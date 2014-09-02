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
import com.rogueai.framework.snmp2bean.api.util.SnmpServiceUtil;
import com.rogueai.framework.snmp2bean.exception.SnmpAnnotationException;
import com.rogueai.framework.snmp2bean.exception.SnmpException;

public class Snmp4JServiceWrite extends Snmp4JService implements SnmpServiceWrite {
    
    public void delete(Object entry) throws IOException, SnmpException,
    SnmpAnnotationException {
        try {
            PDU reqPDU = newDeletPDU(entry);
            checkReqError(reqPDU);
            ResponseEvent event = getSnmp().set(reqPDU, snmpSession.getWriteTarget());
            checkEventError(event);
            PDU resPDU = event.getResponse();
            checkResError(resPDU);
        } catch (IllegalArgumentException e) {
            throw new SnmpAnnotationException(e);
        } catch (IllegalAccessException e) {
            throw new SnmpAnnotationException(e);
        }
    }
    
    public void set(Object entry) throws IOException, SnmpException,
    SnmpAnnotationException {
        try {
            PDU reqPDU = newSetPDU(entry);
            checkReqError(reqPDU);
            ResponseEvent event = getSnmp().set(reqPDU, snmpSession.getWriteTarget());
            checkEventError(event);
            PDU resPDU = event.getResponse();
            checkResError(resPDU);
        } catch (InstantiationException e) {
            throw new SnmpAnnotationException(e);
        } catch (IllegalAccessException e) {
            throw new SnmpAnnotationException(e);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            throw new SnmpAnnotationException(e);
        } catch (NoSuchMethodException e) {
            throw new SnmpAnnotationException(e);
        } catch (InvocationTargetException e) {
            throw new SnmpAnnotationException(e);
        }
    }
    
    public void create(Object entry) throws IOException, SnmpException,
    SnmpAnnotationException {
        try {
            PDU reqPDU = newCreatePDU(entry);
            checkReqError(reqPDU);
            ResponseEvent event = getSnmp().set(reqPDU, snmpSession.getWriteTarget());
            checkEventError(event);
            PDU resPDU = event.getResponse();
            checkResError(resPDU);
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
    
    private VariableBinding newDeleteVB(Object entry)
            throws IllegalAccessException, SnmpAnnotationException {
        OID indexOid = buildIndexOid(entry);
        if (indexOid == null)
            throw new SnmpAnnotationException(new NullPointerException(
                    "No index oid."));
        RowStatus rowStatus = entry.getClass().getAnnotation(RowStatus.class);
        if (rowStatus == null)
            throw new SnmpAnnotationException(new NullPointerException(
                    "No RowStatus Annotation."));
        OID oid = new OID(rowStatus.oid());
        oid.append(indexOid);
        Integer32 var = new Integer32(rowStatus.delete());
        VariableBinding vb = new VariableBinding(oid, var);
        return vb;
    }
    
    
    private PDU newCreatePDU(Object entry) throws IllegalArgumentException,
    SecurityException, IllegalAccessException, NoSuchMethodException,
    InstantiationException, InvocationTargetException,
    SnmpAnnotationException {
        PDU pdu = newSetPDU(entry);
        pdu.add(newCreateVB(entry));
        return pdu;
    }
    
    private VariableBinding newCreateVB(Object entry)
            throws IllegalAccessException, SnmpAnnotationException {
        OID indexOid = buildIndexOid(entry);
        checkIndexOid(indexOid);
        RowStatus rowStatus = entry.getClass().getAnnotation(RowStatus.class);
        checkRowStatusAnnotation(rowStatus);
        OID oid = new OID(rowStatus.oid());
        oid.append(indexOid);
        Integer32 var = new Integer32(rowStatus.create());
        return new VariableBinding(oid, var);
    }
    
    private PDU newSetPDU(Object entry) throws IllegalArgumentException,
    IllegalAccessException, SecurityException, NoSuchMethodException,
    InstantiationException, InvocationTargetException {
        PDU pdu = new PDU();
        pdu.setType(PDU.SET);
        OID indexOid = buildIndexOid(entry);
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
            Variable variable = (Variable) constructor
                    .newInstance(new Object[] { writeField.get(entry) });
            pdu.add(new VariableBinding(oid, variable));
        }
        return pdu;
    }
    
    private PDU newDeletPDU(Object entry) throws IllegalArgumentException,
    IllegalAccessException, SnmpAnnotationException {
        PDU pdu = new PDU();
        pdu.setType(PDU.SET);
        pdu.add(newDeleteVB(entry));
        return pdu;
    }
    
}
