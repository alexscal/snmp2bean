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

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Vector;

import org.snmp4j.PDU;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

import com.rogueai.framework.snmp2bean.annotation.MibIndex;
import com.rogueai.framework.snmp2bean.annotation.MibObjectType;
import com.rogueai.framework.snmp2bean.annotation.RowStatus;
import com.rogueai.framework.snmp2bean.api.SmiTypeProvider;
import com.rogueai.framework.snmp2bean.api.SnmpErrorMsgProvider;
import com.rogueai.framework.snmp2bean.api.SnmpSession;
import com.rogueai.framework.snmp2bean.api.util.SnmpServiceUtil;
import com.rogueai.framework.snmp2bean.enums.SmiType;
import com.rogueai.framework.snmp2bean.exception.SnmpAnnotationException;
import com.rogueai.framework.snmp2bean.exception.SnmpException;

public abstract class AbstractSnmp4JService {
    
    protected abstract SmiTypeProvider getSmiTypeProvider();
    
    protected abstract SnmpErrorMsgProvider getSnmpErrorMsgProvider();
    
    protected SnmpSession snmpSession;
    
    protected PduBuilder pduBuilder;
    
    protected boolean isTableEnd(OID firstReqOid, OID firstRespOid) {
        // TODO: END OF MIB
        return !firstRespOid.startsWith(firstReqOid);
    }
    
    protected void checkRequestError(PDU requestPDU) {
        if (requestPDU.size() == 0) {
            throw new IllegalArgumentException("No declarative mib object.");
        }
    }
    
    protected int[] extractIndexOids(OID firstResponseOID, OID firstRequestOID) {
        int[] respOids = firstResponseOID.getValue();
        int[] reqOids = firstRequestOID.getValue();
        int[] indexOids = new int[respOids.length - reqOids.length];
        System.arraycopy(respOids, reqOids.length, indexOids, 0, indexOids.length);
        return indexOids;
    }
    
    protected void checkResponseError(PDU responsePDU) throws SnmpException {
        if (responsePDU == null) {
            SnmpException e = new SnmpException(SnmpException.NO_RESPONSE_PDU, -1);
            e.setSnmpErrorMsgProvider(getSnmpErrorMsgProvider());
            throw e;
        }
        if (responsePDU.getErrorStatus() != 0) {
            SnmpException e = new SnmpException(responsePDU.getErrorStatus(), responsePDU.getErrorIndex());
            e.setSnmpErrorMsgProvider(getSnmpErrorMsgProvider());
            throw e;
        }
    }
    
    protected void checkEventError(ResponseEvent event) throws SnmpException {
        if (event.getError() != null) {
            SnmpException e = new SnmpException(event.getError());
            e.setSnmpErrorMsgProvider(getSnmpErrorMsgProvider());
            throw e;
        }
    }  
    
    protected void populateOidIndexes(Object entry, int[] indexOids) throws IllegalArgumentException, IllegalAccessException {
        Class<? extends Object> clazz = entry.getClass();
        Field[] indexFields = SnmpServiceUtil.getIndexFields(clazz);
        for (int i = 0, j = 0; i < indexFields.length && j < indexOids.length; i++) {
            Field indexField = indexFields[i];
            indexField.setAccessible(true);
            MibIndex miAnnotation = indexField.getAnnotation(MibIndex.class);
            int length = miAnnotation.length();
            if (length == MibIndex.VARSTR_WITH_LENGTH) {
                byte[] bytes = new byte[indexOids[j++]];
                bytes = SnmpServiceUtil.copyBytes(indexOids, j, bytes);
                indexField.set(entry, new String(bytes));
                j += bytes.length;
            } else if (length == MibIndex.VARSTR_WITHOUT_LENGTH) {
                byte[] bytes = new byte[indexOids.length - j];
                bytes = SnmpServiceUtil.copyBytes(indexOids, j, bytes);
                indexField.set(entry, new String(bytes));
                j += bytes.length;
            } else if (length == 1) {
                MibObjectType mot = indexField
                        .getAnnotation(MibObjectType.class);
                Class<?> smiTypeClass = getSmiTypeProvider().getSmiType(mot.smiType());
                if (smiTypeClass.equals(Integer32.class)) {
                    indexField.set(entry, indexOids[j++]);
                } else if (UnsignedInteger32.class.isAssignableFrom(smiTypeClass)) {
                    indexField.set(entry, (long) indexOids[j++]);
                } else {
                    throw new RuntimeException("Index length should not be 1." + indexField);
                }
            } else if (length >= 1) {
                MibObjectType mot = indexField.getAnnotation(MibObjectType.class);
                SmiType smiType = mot.smiType();
                if (smiType == SmiType.OID) {
                    int[] oidValue = new int[length];
                    System.arraycopy(indexOids, j, oidValue, 0, length);
                    indexField.set(entry, SnmpServiceUtil.intAry2Str(oidValue));
                } else if (smiType == SmiType.DISPLAY_STRING) {
                    byte[] bytes = new byte[length];
                    bytes = SnmpServiceUtil.copyBytes(indexOids, j, bytes);
                    indexField.set(entry, new String(bytes));
                } else if (smiType == SmiType.OCTET_STRING) {
                    byte[] bytes = new byte[length];
                    bytes = SnmpServiceUtil.copyBytes(indexOids, j, bytes);
                    indexField.set(entry, bytes);
                } else if (smiType == SmiType.IPADDRESS) {
                    if (length != 4)
                        throw new RuntimeException("Asser Failed, IpAddress length must be 4. length=" + length);
                    StringBuffer sb = new StringBuffer();
                    sb.append(indexOids[j]).append('.');
                    sb.append(indexOids[j + 1]).append('.');
                    sb.append(indexOids[j + 2]).append('.');
                    sb.append(indexOids[j + 3]);
                    indexField.set(entry, sb.toString());
                } else {
                    throw new RuntimeException("Unknow smiType: " + smiType);
                }
                j += length;
            } else {
                throw new RuntimeException("Assert Failed! Unknow index length.");
            }
        }
    }
    
    protected void populateProperties(Object object, PDU pdu) throws InstantiationException, IllegalAccessException {
        Field[] propFields = SnmpServiceUtil.getPropFields(object.getClass());
        Vector<? extends VariableBinding> variableBindings = pdu.getVariableBindings();
        for (Field propField : propFields) {
            propField.setAccessible(true);
            MibObjectType mot = propField.getAnnotation(MibObjectType.class);
            OID oid = new OID(mot.oid());
            VariableBinding variableBinding = findVariableBindingByOid(oid, variableBindings);
            if (variableBinding != null) {
                Object value = null;
                
                if (mot.smiType() == SmiType.OID) {
                    value = variableBinding.getOid().toString();
                }
                else 
                    value = getValueFromVariableAndType(variableBinding.getVariable(), mot);
                
                if (value != null) propField.set(object, value);
            }
        }
    }
    
    private Object getValueFromVariableAndType(Variable variable, MibObjectType mot) {
        Object value = null;
        
        switch (mot.smiType()) {
            case INTEGER:
            case INTEGER32:
                value = variable.toInt();
                break;
            case TIMETICKS:
            case UNSIGNED32:
            case COUNTER32:
            case COUNTER64:
            case GAUGE32:
                value = variable.toLong();
                break;
            case OCTET_STRING:
                value = variable.toString().getBytes();
                break;
            default:
                value = variable.toString();
        }
        
        return value;
    }
    
    protected Variable findVariableByOid(OID oid, Vector<?> variableBindings) {
        for (Iterator<?> it = variableBindings.iterator(); it.hasNext();) {
            VariableBinding vb = (VariableBinding) it.next();
            if (vb.getOid().startsWith(oid)) {
                return vb.getVariable();
            }
        }
        return null;
    }
    
    protected VariableBinding findVariableBindingByOid(OID oid, Vector<?> variableBindings) {
        for (Iterator<?> it = variableBindings.iterator(); it.hasNext();) {
            VariableBinding vb = (VariableBinding) it.next();
            if (vb.getOid().startsWith(oid)) {
                return vb;
            }
        }
        return null;
    }
    
    protected void checkRowStatusAnnotation(RowStatus rowStatus) throws SnmpAnnotationException {
        if (rowStatus == null) {
            throw new SnmpAnnotationException(new NullPointerException(
                    "No RowStatus Annotation."));
        }
    }
    
    protected void checkIndexOid(OID indexOid) throws SnmpAnnotationException {
        if (indexOid == null) {
            throw new SnmpAnnotationException(new NullPointerException(
                    "No index oid."));
        }
    }
    
    public SnmpSession getSnmpSession() {
        return snmpSession;
    }
    
    public void setSnmpSession(SnmpSession snmpSession) {
        this.snmpSession = snmpSession;
        pduBuilder = new PduBuilder(getSmiTypeProvider());
    }
}