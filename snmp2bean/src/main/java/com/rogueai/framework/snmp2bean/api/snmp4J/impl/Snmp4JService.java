/*******************************************************************************
 * Copyright 2014 Rogueai.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.rogueai.framework.snmp2bean.api.snmp4J.impl;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.OID;

import com.rogueai.framework.snmp2bean.api.SmiTypeProvider;
import com.rogueai.framework.snmp2bean.api.SnmpErrorMsgProvider;
import com.rogueai.framework.snmp2bean.api.SnmpService;
import com.rogueai.framework.snmp2bean.api.SnmpSession;
import com.rogueai.framework.snmp2bean.api.util.SnmpServiceUtil;
import com.rogueai.framework.snmp2bean.exception.SnmpAnnotationException;
import com.rogueai.framework.snmp2bean.exception.SnmpException;

public class Snmp4JService extends AbstractSnmp4JService implements SnmpService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Snmp4JService.class);
    
    public Snmp4JService(SnmpSession snmpSession) {
        setSnmpSession(snmpSession);
    }
    
    protected Snmp4JWrapper getSnmp() {
        return snmpSession.getSnmpWrapper();
    }
    
    public <T> T get(Class<T> scalarClass) throws IOException, SnmpException,  SnmpAnnotationException {
        if (LOGGER.isDebugEnabled()) 
            LOGGER.debug("Executing get for "+ scalarClass);
        PDU requestPDU = pduBuilder.buildGetPDU(scalarClass);
        return get(scalarClass, requestPDU);
    }
    
    public <T> T get(Class<T> scalarClass, String[] fields) throws IOException, SnmpException, SnmpAnnotationException {
        try {
            PDU requestPDU = pduBuilder.buildGetPDU(scalarClass, fields);
            return get(scalarClass, requestPDU);
        } catch (SecurityException | NoSuchFieldException e) {
            throw new SnmpAnnotationException(e);
        }
    }
    
    public <T> List<T> getTable(Class<T> entryClass) throws IOException, SnmpException, SnmpAnnotationException {
        try {
            List<T> list = new ArrayList<T>();
            PDU requestPDU = pduBuilder.buildGetNextFirstEntryPDU(entryClass);
            checkRequestError(requestPDU);
            OID firstRequestOID = requestPDU.get(0).getOid();
            while (true) {
                ResponseEvent event = getSnmp().getNext(requestPDU, snmpSession.getReadTarget());
                checkEventError(event);
                PDU responsePDU = event.getResponse();
                checkResponseError(responsePDU);
                OID firstResponseOID = responsePDU.get(0).getOid();
                if (isTableEnd(firstRequestOID, firstResponseOID)) {
                    break;
                }
                int[] indexOIDs = extractIndexOids(firstResponseOID, firstRequestOID);
                T entry = entryClass.newInstance();
                populateOidIndexes(entry, indexOIDs);
                populateProperties(entry, responsePDU);
                list.add(entry);
                requestPDU = pduBuilder.buildGetNextEntryPDU(entry);
            }
            return list;
        } catch (IllegalArgumentException | InstantiationException | IllegalAccessException e) {
            throw new SnmpAnnotationException(e);
        }
    }
    
    public <T> T getByIndex(Class<T> entryClass, Serializable indexes) throws IOException, SnmpException, SnmpAnnotationException {
        try {
            T entry = buildEntryWithIndexes(entryClass, indexes);
            PDU requestPDU = pduBuilder.buildGetEntryPDU(entry);
            return getEntryByIndex(entry, requestPDU);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new SnmpAnnotationException(e);
        }
    }
    
    public <T> T getByIndex(Class<T> entryClass, Serializable indexes, String[] fields) throws IOException, SnmpException, SnmpAnnotationException {
        try {
            T entry = buildEntryWithIndexes(entryClass, indexes);
            PDU reqPDU = pduBuilder.buildGetEntryPDU(entry, fields);
            return getEntryByIndex(entry, reqPDU);
        } catch (IllegalArgumentException | SecurityException | IllegalAccessException | NoSuchFieldException | InstantiationException e) {
            throw new SnmpAnnotationException(e);
        }
    }
    
    public void close() throws IOException {
        getSnmp().close();
    }
    
    private <T> T get(Class<T> scalarClass, PDU requestPDU) throws IOException, SnmpException, SnmpAnnotationException {
        try {
            checkRequestError(requestPDU);
            ResponseEvent event = getSnmp().get(requestPDU, snmpSession.getReadTarget());
            checkEventError(event);
            PDU responsePDU = event.getResponse();
            checkResponseError(responsePDU);
            T mibObj = scalarClass.newInstance();
            populateProperties(mibObj, responsePDU);
            return mibObj;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new SnmpAnnotationException(e);
        }
    }
    
    private <T> T getEntryByIndex(T entry, PDU requestPDU) throws IOException, SnmpException, InstantiationException, IllegalAccessException {
        checkRequestError(requestPDU);
        ResponseEvent event = getSnmp().get(requestPDU, snmpSession.getReadTarget());
        checkEventError(event);
        PDU responsePDU = event.getResponse();
        checkResponseError(responsePDU);
        populateProperties(entry, responsePDU);
        return entry;
    }
    
    private <T> T buildEntryWithIndexes(Class<T> entryClass, Serializable indexes) throws InstantiationException, IllegalAccessException {
        T entry = entryClass.newInstance();
        Field[] indexFields = SnmpServiceUtil.getIndexFields(entryClass);
        if (indexFields.length == 1) {
            indexFields[0].set(entry, indexes);
        } else {
            Serializable[] indicesArray = (Serializable[]) indexes;
            for (int i = 0; i < indexFields.length; i++) {
                indexFields[i].set(entry, indicesArray[i]);
            }
        }
        return entry;
    }
    
    @Override
    protected SmiTypeProvider getSmiTypeProvider() {
        return snmpSession.getSmiTypeProvider();
    }
    
    @Override
    protected SnmpErrorMsgProvider getSnmpErrorMsgProvider() {
        return snmpSession.getSnmpErrorMsgProvider();
    }
}