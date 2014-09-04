package com.rogueai.framework.snmp2bean.api.snmp4J.impl;

import java.lang.reflect.Field;

import org.snmp4j.PDU;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

import com.rogueai.framework.snmp2bean.annotation.MibObjectType;
import com.rogueai.framework.snmp2bean.api.SmiTypeProvider;
import com.rogueai.framework.snmp2bean.api.util.SnmpServiceUtil;

public class PduBuilder extends AbstractBuilder {
    
    public PduBuilder(SmiTypeProvider smiTypeProvider) {
        setSmiTypeProvider(smiTypeProvider);
    }
    
    public PDU buildGetPDU(Class scalarClass) {
        PDU pdu = new PDU();
        pdu.setType(PDU.GET);
        Field[] propFields = SnmpServiceUtil.getPropFields(scalarClass);
        for (Field propField : propFields) {
            MibObjectType mib = propField.getAnnotation(MibObjectType.class);
            pdu.add(new VariableBinding(new OID(mib.oid())));
        }
        return pdu;
    }
    
    public PDU buildGetPDU(Class scalarClass, String[] fields) throws SecurityException, NoSuchFieldException {
        PDU pdu = new PDU();
        pdu.setType(PDU.GET);
        for (String fn : fields) {
            Field field = scalarClass.getDeclaredField(fn);
            MibObjectType mib = field.getAnnotation(MibObjectType.class);
            pdu.add(new VariableBinding(new OID(mib.oid())));
        }
        return pdu;
    }
    
    public PDU buildGetEntryPDU(Object entry) throws IllegalArgumentException, IllegalAccessException {
        PDU pdu = new PDU();
        pdu.setType(PDU.GET);
        OID indexOid = buildIndexOID(entry);
        Field[] propFields = SnmpServiceUtil.getPropFields(entry.getClass());
        for (Field propField : propFields) {
            MibObjectType mib = propField.getAnnotation(MibObjectType.class);
            OID oid = new OID(mib.oid());
            if (indexOid != null) {
                oid.append(indexOid);
            }
            pdu.add(new VariableBinding(oid));
        }
        return pdu;
    }
    
    public PDU buildGetNextFirstEntryPDU(Class entryClass) {
        PDU pdu = new PDU();
        pdu.setType(PDU.GETNEXT);
        Field[] propFields = SnmpServiceUtil.getPropFields(entryClass);
        for (Field propField : propFields) {
            MibObjectType mib = propField.getAnnotation(MibObjectType.class);
            pdu.add(new VariableBinding(new OID(mib.oid())));
        }
        if (pdu.size() <= 0) {
            Field[] indexFields = SnmpServiceUtil.getIndexFields(entryClass);
            if (indexFields.length > 0) {
                MibObjectType mot = indexFields[0].getAnnotation(MibObjectType.class);
                pdu.add(new VariableBinding(new OID(mot.oid())));
            }
        }
        return pdu;
    }
    
    public PDU buildGetNextEntryPDU(Object entry) throws IllegalArgumentException, IllegalAccessException {
        PDU pdu = buildGetEntryPDU(entry);
        pdu.setType(PDU.GETNEXT);
        if (pdu.size() <= 0) {
            OID indexOid = buildIndexOID(entry);
            Field[] indexFields = SnmpServiceUtil.getIndexFields(entry.getClass());
            if (indexFields.length > 0) {
                MibObjectType mib = indexFields[0] .getAnnotation(MibObjectType.class);
                OID oid = new OID(mib.oid());
                if (indexOid != null) {
                    oid.append(indexOid);
                }
                pdu.add(new VariableBinding(oid));
            }
        }
        return pdu;
    }
    
    public PDU buildGetEntryPDU(Object entry, String[] fields) throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException {
        PDU pdu = new PDU();
        pdu.setType(PDU.GET);
        OID indexOid = buildIndexOID(entry);
        for (String fn : fields) {
            Field field = entry.getClass().getDeclaredField(fn);
            MibObjectType mib = field.getAnnotation(MibObjectType.class);
            OID oid = new OID(mib.oid());
            if (indexOid != null) {
                oid.append(indexOid);
            }
            pdu.add(new VariableBinding(oid));
        }
        return pdu;
    }
}