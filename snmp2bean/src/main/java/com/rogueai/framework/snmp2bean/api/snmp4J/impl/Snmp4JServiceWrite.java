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
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.snmp4j.PDU;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

import com.rogueai.framework.snmp2bean.annotation.MibObjectType;
import com.rogueai.framework.snmp2bean.annotation.RowStatus;
import com.rogueai.framework.snmp2bean.api.SnmpServiceWrite;
import com.rogueai.framework.snmp2bean.api.SnmpSession;
import com.rogueai.framework.snmp2bean.api.util.SnmpServiceUtil;
import com.rogueai.framework.snmp2bean.exception.SnmpAnnotationException;
import com.rogueai.framework.snmp2bean.exception.SnmpException;

public class Snmp4JServiceWrite extends Snmp4JService implements SnmpServiceWrite {
    
    public Snmp4JServiceWrite(SnmpSession snmpSession) {
        super(snmpSession);
    }

    public void delete(Object entry) throws IOException, SnmpException, SnmpAnnotationException {
        try {
            PDU requestPDU = buildDeletPDU(entry);
            checkRequestError(requestPDU);
            ResponseEvent event = getSnmp().set(requestPDU, snmpSession.getWriteTarget());
            checkEventError(event);
            PDU responsePDU = event.getResponse();
            checkResponseError(responsePDU);
        } catch (IllegalArgumentException e) {
            throw new SnmpAnnotationException(e);
        } catch (IllegalAccessException e) {
            throw new SnmpAnnotationException(e);
        }
    }
    
    public void set(Object entry) throws IOException, SnmpException, SnmpAnnotationException {
        try {
            PDU requestPDU = buildSetPDU(entry);
            checkRequestError(requestPDU);
            ResponseEvent event = getSnmp().set(requestPDU, snmpSession.getWriteTarget());
            checkEventError(event);
            PDU responsePDU = event.getResponse();
            checkResponseError(responsePDU);
        } catch (InstantiationException e) {
            throw new SnmpAnnotationException(e);
        } catch (IllegalAccessException e) {
            throw new SnmpAnnotationException(e);
        } catch (IllegalArgumentException e) {
            throw new SnmpAnnotationException(e);
        } catch (SecurityException e) {
            throw new SnmpAnnotationException(e);
        } catch (NoSuchMethodException e) {
            throw new SnmpAnnotationException(e);
        } catch (InvocationTargetException e) {
            throw new SnmpAnnotationException(e);
        }
    }
    
    public void create(Object entry) throws IOException, SnmpException, SnmpAnnotationException {
        try {
            PDU requestPDU = buildCreatePDU(entry);
            checkRequestError(requestPDU);
            ResponseEvent event = getSnmp().set(requestPDU, snmpSession.getWriteTarget());
            checkEventError(event);
            PDU responsePDU = event.getResponse();
            checkResponseError(responsePDU);
        } catch (IllegalArgumentException e) {
            throw new SnmpAnnotationException(e);
        } catch (IllegalAccessException e) {
            throw new SnmpAnnotationException(e);
        } catch (SecurityException e) {
            throw new SnmpAnnotationException(e);
        } catch (NoSuchMethodException e) {
            throw new SnmpAnnotationException(e);
        } catch (InstantiationException e) {
            throw new SnmpAnnotationException(e);
        } catch (InvocationTargetException e) {
            throw new SnmpAnnotationException(e);
        }
    }
    
    private VariableBinding buildDeleteVariableBinding(Object entry) throws IllegalAccessException, SnmpAnnotationException {
        OID indexOid = pduBuilder.buildIndexOID(entry);
        if (indexOid == null)
            throw new SnmpAnnotationException(new NullPointerException("No index oid."));
        RowStatus rowStatus = entry.getClass().getAnnotation(RowStatus.class);
        if (rowStatus == null)
            throw new SnmpAnnotationException(new NullPointerException("No RowStatus Annotation."));
        OID oid = new OID(rowStatus.oid());
        oid.append(indexOid);
        Integer32 var = new Integer32(rowStatus.delete());
        VariableBinding vb = new VariableBinding(oid, var);
        return vb;
    }
    
    private PDU buildCreatePDU(Object entry) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException, SnmpAnnotationException {
        PDU pdu = buildSetPDU(entry);
        pdu.add(buildCreateVariableBinding(entry));
        return pdu;
    }
    
    private VariableBinding buildCreateVariableBinding(Object entry) throws IllegalAccessException, SnmpAnnotationException {
        OID indexOid = pduBuilder.buildIndexOID(entry);
        checkIndexOid(indexOid);
        RowStatus rowStatus = entry.getClass().getAnnotation(RowStatus.class);
        checkRowStatusAnnotation(rowStatus);
        OID oid = new OID(rowStatus.oid());
        oid.append(indexOid);
        Integer32 var = new Integer32(rowStatus.create());
        return new VariableBinding(oid, var);
    }
    
    private PDU buildSetPDU(Object entry) throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        PDU pdu = new PDU();
        pdu.setType(PDU.SET);
        OID indexOid = pduBuilder.buildIndexOID(entry);
        Field[] writePropFields = SnmpServiceUtil.getWritePropFields(entry.getClass());
        for (Field writeField : writePropFields) {
            writeField.setAccessible(true);
            MibObjectType mot = writeField.getAnnotation(MibObjectType.class);
            OID oid = new OID(mot.oid());
            if (indexOid != null) {
                oid.append(indexOid);
            }
            Class type = writeField.getType();
            Constructor constructor = getSmiTypeProvider().getSmiType(mot.smiType()).getConstructor(new Class[] { type });
            Variable variable = (Variable) constructor.newInstance(new Object[] { writeField.get(entry) });
            pdu.add(new VariableBinding(oid, variable));
        }
        return pdu;
    }
    
    private PDU buildDeletPDU(Object entry) throws IllegalArgumentException, IllegalAccessException, SnmpAnnotationException {
        PDU pdu = new PDU();
        pdu.setType(PDU.SET);
        pdu.add(buildDeleteVariableBinding(entry));
        return pdu;
    }
}