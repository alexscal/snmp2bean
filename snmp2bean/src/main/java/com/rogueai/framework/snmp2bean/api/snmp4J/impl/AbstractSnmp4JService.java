package com.rogueai.framework.snmp2bean.api.snmp4J.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    
    protected boolean isTableEnd(OID firstReqOid, OID firstRespOid) {
        // TODO: END OF MIB
        return !firstRespOid.startsWith(firstReqOid);
    }
    
    protected void checkReqError(PDU reqPDU) {
        if (reqPDU.size() == 0) {
            throw new IllegalArgumentException("No declarative mib object.");
        }
    }
    
    protected int[] extractIndexOids(OID firstRespOid, OID firstReqOid) {
        int[] respOids = firstRespOid.getValue();
        int[] reqOids = firstReqOid.getValue();
        int[] indexOids = new int[respOids.length - reqOids.length];
        System.arraycopy(respOids, reqOids.length, indexOids, 0,
                indexOids.length);
        return indexOids;
    }
    
    protected void checkResError(PDU resPDU) throws SnmpException {
        if (resPDU == null) {
            SnmpException e = new SnmpException(SnmpException.NO_RESPONSE_PDU, -1);
            e.setSnmpErrorMsgProvider(getSnmpErrorMsgProvider());
            throw e;
        }
        if (resPDU.getErrorStatus() != 0) {
            SnmpException e = new SnmpException(resPDU.getErrorStatus(), resPDU
                    .getErrorIndex());
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
    
    
    
    
    protected Field[] getIndexFields(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> list = new ArrayList<Field>();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isAnnotationPresent(MibIndex.class)) {
                fields[i].setAccessible(true);
                list.add(fields[i]);
            }
        }
        Field[] indexFields = list.toArray(new Field[list.size()]);
        // bubble sort.
        for (int i = 0; i < indexFields.length; i++) {
            for (int j = i + 1; j < indexFields.length; j++) {
                Field top = indexFields[i];
                int no = top.getAnnotation(MibIndex.class).no();
                Field another = indexFields[j];
                int anotherNo = another.getAnnotation(MibIndex.class).no();
                if (anotherNo < no) {
                    indexFields[i] = another;
                    indexFields[j] = top;
                }
            }
        }
        return indexFields;
    }
    

    
    protected OID buildIndexOid(Object entry) throws IllegalArgumentException,
    IllegalAccessException {
        Class clazz = entry.getClass();
        Field[] indexFields = getIndexFields(clazz);
        OID oid = null;
        for (Field indexField : indexFields) {
            indexField.setAccessible(true);
            Object value = indexField.get(entry);
            MibIndex miAnnotation = indexField.getAnnotation(MibIndex.class);
            int length = miAnnotation.length();
            if (length == MibIndex.VARSTR_WITH_LENGTH) {
                byte[] bytes = ((String) value).getBytes();
                int[] integers = new int[bytes.length + 1];
                integers[0] = bytes.length;
                int i = 1;
                for (byte b : bytes) {
                    integers[i++] = b;
                }
                oid = appendRawOids(oid, integers);
            } else if (length == MibIndex.VARSTR_WITHOUT_LENGTH) {
                byte[] bytes = ((String) value).getBytes();
                int[] integers = new int[bytes.length];
                int i = 0;
                for (byte b : bytes) {
                    integers[i++] = b;
                }
                oid = appendRawOids(oid, integers);
            } else if (length == 1) {
                MibObjectType mot = indexField
                        .getAnnotation(MibObjectType.class);
                Class smiTypeClass = getSmiTypeProvider().getSmiType(
                        mot.smiType());
                if (smiTypeClass.equals(Integer32.class)) {
                    int v = ((Integer) value).intValue();
                    oid = appendRawOids(oid, new int[] { v });
                } else if (UnsignedInteger32.class
                        .isAssignableFrom(smiTypeClass)) {
                    int v = (int) ((Long) value).longValue();
                    oid = appendRawOids(oid, new int[] { v });
                } else {
                    throw new RuntimeException("Index length should not be 1."
                            + indexField);
                }
            } else if (length >= 1) {
                MibObjectType mot = indexField
                        .getAnnotation(MibObjectType.class);
                SmiType smiType = mot.smiType();
                if (smiType == SmiType.OID) {
                    if (oid == null)
                        oid = new OID();
                    oid.append((String) value);
                } else if (smiType == SmiType.DISPLAY_STRING) {
                    byte[] bytes = ((String) value).getBytes();
                    int[] integers = new int[length];
                    for (int i = 0; i < integers.length; i++) {
                        integers[i] = (int) bytes[i];
                    }
                    oid = appendRawOids(oid, integers);
                } else if (smiType == SmiType.OCTET_STRING) {
                    byte[] bytes = ((byte[]) value);
                    int[] integers = new int[length];
                    for (int i = 0; i < integers.length; i++) {
                        integers[i] = (int) bytes[i];
                    }
                    oid = appendRawOids(oid, integers);
                } else if (smiType == SmiType.IPADDRESS) {
                    String[] strBytes = ((String) value).split("\\.");
                    if (strBytes.length != 4)
                        throw new RuntimeException(
                                "Assert faild. IpAddres length must be 1.");
                    int[] integers = new int[strBytes.length];
                    for (int i = 0; i < integers.length; i++) {
                        integers[i] = Integer.parseInt(strBytes[i]);
                    }
                    oid = appendRawOids(oid, integers);
                } else {
                    throw new RuntimeException("Unknow smiType: " + smiType);
                }
            } else {
                throw new RuntimeException(
                        "Assert Failed! Unknow index length.");
            }
        }
        return oid;
    }
    
    protected OID appendRawOids(OID oid, int[] integers) {
        if (oid == null)
            return new OID(integers);
        oid.append(new OID(integers));
        return oid;
    }
    
    protected void fillIndices(Object entry, int[] indexOids)
            throws IllegalArgumentException, IllegalAccessException {
        Class clazz = entry.getClass();
        Field[] indexFields = getIndexFields(clazz);
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
                Class smiTypeClass = getSmiTypeProvider().getSmiType(
                        mot.smiType());
                if (smiTypeClass.equals(Integer32.class)) {
                    indexField.set(entry, indexOids[j++]);
                } else if (UnsignedInteger32.class
                        .isAssignableFrom(smiTypeClass)) {
                    indexField.set(entry, (long) indexOids[j++]);
                } else {
                    throw new RuntimeException("Index length should not be 1."
                            + indexField);
                }
            } else if (length >= 1) {
                MibObjectType mot = indexField
                        .getAnnotation(MibObjectType.class);
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
                        throw new RuntimeException(
                                "Asser Failed, IpAddress length must be 4. length="
                                        + length);
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
                throw new RuntimeException(
                        "Assert Failed! Unknow index length.");
            }
        }
    }
    
    
    
    protected void fillProperties(Object object, PDU pdu)
            throws InstantiationException, IllegalAccessException {
        Field[] propFields = SnmpServiceUtil.getPropFields(object.getClass());
        Vector<? extends VariableBinding> variableBindings = pdu.getVariableBindings();
        for (Field propField : propFields) {
            propField.setAccessible(true);
            MibObjectType mot = propField.getAnnotation(MibObjectType.class);
            OID oid = new OID(mot.oid());
            VariableBinding variable = findVariableBindingByOid(oid, variableBindings);
            if (variable != null) {
                Object value = null;
                if (mot.smiType() == SmiType.DISPLAY_STRING) {
                    value = variable.getVariable().toString();
                } else if (mot.smiType() == SmiType.OID) {
                    value = variable.getOid().toString();
                }
                if (value != null) propField.set(object, value);
            }
        }
    }
    
    protected Variable findVariableByOid(OID oid, Vector variableBindings) {
        for (Iterator it = variableBindings.iterator(); it.hasNext();) {
            VariableBinding vb = (VariableBinding) it.next();
            if (vb.getOid().startsWith(oid)) {
                return vb.getVariable();
            }
        }
        return null;
    }
    
    protected VariableBinding findVariableBindingByOid(OID oid, Vector variableBindings) {
        for (Iterator it = variableBindings.iterator(); it.hasNext();) {
            VariableBinding vb = (VariableBinding) it.next();
            if (vb.getOid().startsWith(oid)) {
                return vb;
            }
        }
        return null;
    }
    
    protected void checkRowStatusAnnotation(RowStatus rowStatus)
            throws SnmpAnnotationException {
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
    }
    
}
