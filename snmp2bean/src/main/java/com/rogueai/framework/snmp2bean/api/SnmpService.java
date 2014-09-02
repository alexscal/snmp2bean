package com.rogueai.framework.snmp2bean.api;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.rogueai.framework.snmp2bean.exception.SnmpAnnotationException;
import com.rogueai.framework.snmp2bean.exception.SnmpException;

public interface SnmpService {
    
    <T> T get(Class<T> scalarClass) throws IOException, SnmpException, SnmpAnnotationException;

    <T> T get(Class<T> scalarClass, String[] fields) throws IOException, SnmpException, SnmpAnnotationException;
    
    <T> List<T> getTable(Class<T> entryClass) throws IOException, SnmpException, SnmpAnnotationException;

    <T> T getByIndex(Class<T> entryClass, Serializable indices) throws IOException, SnmpException, SnmpAnnotationException;

    <T> T getByIndex(Class<T> entryClass, Serializable indices, String[] fields) throws IOException, SnmpException, SnmpAnnotationException;    
}
