package com.rogueai.framework.snmp2bean.api.snmp4J.impl;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

import com.rogueai.framework.snmp2bean.annotation.MibObjectType;
import com.rogueai.framework.snmp2bean.api.SmiTypeProvider;
import com.rogueai.framework.snmp2bean.api.SnmpErrorMsgProvider;
import com.rogueai.framework.snmp2bean.api.SnmpService;
import com.rogueai.framework.snmp2bean.api.SnmpSession;
import com.rogueai.framework.snmp2bean.api.util.SnmpServiceUtil;
import com.rogueai.framework.snmp2bean.exception.SnmpAnnotationException;
import com.rogueai.framework.snmp2bean.exception.SnmpException;

public class Snmp4JService extends AbstractSnmp4JService implements SnmpService {
       
    protected Snmp getSnmp() {
        return snmpSession.getSnmp();
    }
    
    public <T> T get(Class<T> scalarClass) throws IOException, SnmpException,  SnmpAnnotationException {
        PDU reqPDU = newGetPDU(scalarClass);
        return get(scalarClass, reqPDU);
    }
    
    public <T> T get(Class<T> scalarClass, String[] fields) throws IOException,
    SnmpException, SnmpAnnotationException {
        try {
            PDU reqPDU = newGetPDU(scalarClass, fields);
            return get(scalarClass, reqPDU);
        } catch (SecurityException e) {
            throw new SnmpAnnotationException(e);
        } catch (NoSuchFieldException e) {
            throw new SnmpAnnotationException(e);
        }
    }
    
    public <T> List<T> getTable(Class<T> entryClass) throws IOException, SnmpException,
    SnmpAnnotationException {
        try {
            List<T> list = new ArrayList<T>();
            // init pdu
            PDU reqPDU = newGetNextFirstEntryPDU(entryClass);
            checkReqError(reqPDU);
            OID firstReqOid = reqPDU.get(0).getOid();
            while (true) {
                ResponseEvent event = getSnmp().getNext(reqPDU, snmpSession.getReadTarget());
                checkEventError(event);
                PDU respPDU = event.getResponse();
                checkResError(respPDU);
                OID firstRespOid = respPDU.get(0).getOid();
                if (isTableEnd(firstReqOid, firstRespOid)) {
                    break;
                }
                int[] indexOids = extractIndexOids(firstRespOid, firstReqOid);
                T entry = entryClass.newInstance();
                fillIndices(entry, indexOids);
                fillProperties(entry, respPDU);
                list.add(entry);
                reqPDU = newGetNextEntryPDU(entry);
            }
            return list;
        } catch (IllegalArgumentException e) {
            throw new SnmpAnnotationException(e);
        } catch (InstantiationException e) {
            throw new SnmpAnnotationException(e);
        } catch (IllegalAccessException e) {
            throw new SnmpAnnotationException(e);
        }
    }
    
    public <T> T getByIndex(Class<T> entryClass, Serializable indices)
            throws IOException, SnmpException, SnmpAnnotationException {
        try {
            T entry = buildEntryWithIndices(entryClass, indices);
            PDU reqPDU = newGetEntryPDU(entry);
            return getEntryByIndex(entry, reqPDU);
        } catch (InstantiationException e) {
            throw new SnmpAnnotationException(e);
        } catch (IllegalAccessException e) {
            throw new SnmpAnnotationException(e);
        }
    }
    
    public <T> T getByIndex(Class<T> entryClass, Serializable indices,
            String[] fields) throws IOException, SnmpException,
            SnmpAnnotationException {
        try {
            T entry = buildEntryWithIndices(entryClass, indices);
            PDU reqPDU = newGetEntryPDU(entry, fields);
            return getEntryByIndex(entry, reqPDU);
        } catch (IllegalArgumentException e) {
            throw new SnmpAnnotationException(e);
        } catch (SecurityException e) {
            throw new SnmpAnnotationException(e);
        } catch (IllegalAccessException e) {
            throw new SnmpAnnotationException(e);
        } catch (NoSuchFieldException e) {
            throw new SnmpAnnotationException(e);
        } catch (InstantiationException e) {
            throw new SnmpAnnotationException(e);
        }
    }
    
    
    
    
    
    public void close() throws IOException {
        getSnmp().close();
    }
    
    
    
    
    
    
    
    
    

    
    
    
    private PDU newGetPDU(Class scalarClass) {
        PDU pdu = new PDU();
        pdu.setType(PDU.GET);
        Field[] propFields = SnmpServiceUtil.getPropFields(scalarClass);
        for (Field propField : propFields) {
            MibObjectType mib = propField.getAnnotation(MibObjectType.class);
            pdu.add(new VariableBinding(new OID(mib.oid())));
        }
        return pdu;
    }
    
    private PDU newGetPDU(Class scalarClass, String[] fields)
            throws SecurityException, NoSuchFieldException {
        PDU pdu = new PDU();
        pdu.setType(PDU.GET);
        for (String fn : fields) {
            Field field = scalarClass.getDeclaredField(fn);
            MibObjectType mib = field.getAnnotation(MibObjectType.class);
            pdu.add(new VariableBinding(new OID(mib.oid())));
        }
        return pdu;
    }
    
    private <T> T get(Class<T> scalarClass, PDU reqPDU) throws IOException,
    SnmpException, SnmpAnnotationException {
        try {
            checkReqError(reqPDU);
            ResponseEvent event = getSnmp().get(reqPDU, snmpSession.getReadTarget());
            checkEventError(event);
            PDU resPDU = event.getResponse();
            checkResError(resPDU);
            T mibObj = scalarClass.newInstance();
            fillProperties(mibObj, resPDU);
            return mibObj;
        } catch (InstantiationException e) {
            throw new SnmpAnnotationException(e);
        } catch (IllegalAccessException e) {
            throw new SnmpAnnotationException(e);
        }
    }
    
    private PDU newGetEntryPDU(Object entry) throws IllegalArgumentException,
    IllegalAccessException {
        PDU pdu = new PDU();
        pdu.setType(PDU.GET);
        OID indexOid = buildIndexOid(entry);
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
    
    private PDU newGetNextFirstEntryPDU(Class entryClass) {
        PDU pdu = new PDU();
        pdu.setType(PDU.GETNEXT);
        Field[] propFields = SnmpServiceUtil.getPropFields(entryClass);
        for (Field propField : propFields) {
            MibObjectType mib = propField.getAnnotation(MibObjectType.class);
            pdu.add(new VariableBinding(new OID(mib.oid())));
        }
        if (pdu.size() <= 0) {
            // in some mib, there are only indices.
            Field[] indexFields = getIndexFields(entryClass);
            if (indexFields.length > 0) {
                MibObjectType mot = indexFields[0]
                        .getAnnotation(MibObjectType.class);
                pdu.add(new VariableBinding(new OID(mot.oid())));
            }
        }
        return pdu;
    }
    
    private PDU newGetNextEntryPDU(Object entry)
            throws IllegalArgumentException, IllegalAccessException {
        PDU pdu = newGetEntryPDU(entry);
        pdu.setType(PDU.GETNEXT);
        if (pdu.size() <= 0) {
            OID indexOid = buildIndexOid(entry);
            Field[] indexFields = getIndexFields(entry.getClass());
            if (indexFields.length > 0) {
                MibObjectType mib = indexFields[0]
                        .getAnnotation(MibObjectType.class);
                OID oid = new OID(mib.oid());
                if (indexOid != null) {
                    oid.append(indexOid);
                }
                pdu.add(new VariableBinding(oid));
            }
        }
        return pdu;
    }
    
    private PDU newGetEntryPDU(Object entry, String[] fields)
            throws IllegalArgumentException, IllegalAccessException,
            SecurityException, NoSuchFieldException {
        PDU pdu = new PDU();
        pdu.setType(PDU.GET);
        OID indexOid = buildIndexOid(entry);
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
    
    private <T> T getEntryByIndex(T entry, PDU reqPDU)
            throws IOException, SnmpException, InstantiationException,
            IllegalAccessException {
        checkReqError(reqPDU);
        ResponseEvent event = getSnmp().get(reqPDU, snmpSession.getReadTarget());
        checkEventError(event);
        PDU resPDU = event.getResponse();
        checkResError(resPDU);
        fillProperties(entry, resPDU);
        return entry;
    }
    
    private <T> T buildEntryWithIndices(Class<T> entryClass, Serializable indices)
            throws InstantiationException, IllegalAccessException {
        T entry = entryClass.newInstance();
        Field[] indexFields = getIndexFields(entryClass);
        if (indexFields.length == 1) {
            indexFields[0].set(entry, indices);
        } else {
            Serializable[] indicesArray = (Serializable[]) indices;
            for (int i = 0; i < indexFields.length; i++) {
                indexFields[i].set(entry, indicesArray[i]);
            }
        }
        return entry;
    }
    
    
    
    
    
   

    public SnmpSession getSnmpSession() {
        return snmpSession;
    }

    public void setSnmpSession(SnmpSession snmpSession) {
        this.snmpSession = snmpSession;
    }

    @Override
    protected SmiTypeProvider getSmiTypeProvider() {
        return ((Snmp4JSession)snmpSession).getSmiTypeProvider();
    }

    @Override
    protected SnmpErrorMsgProvider getSnmpErrorMsgProvider() {
        return ((Snmp4JSession)snmpSession).getSnmpErrorMsgProvider();
    }
    
}
