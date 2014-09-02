package com.rogueai.framework.snmp2bean.api;

import java.io.IOException;

import com.rogueai.framework.snmp2bean.exception.SnmpAnnotationException;
import com.rogueai.framework.snmp2bean.exception.SnmpException;

public interface SnmpServiceWrite {
    
    void set(Object object) throws IOException, SnmpException,  SnmpAnnotationException;

    void create(Object entry) throws IOException, SnmpException,  SnmpAnnotationException;

    void delete(Object entry) throws IOException, SnmpException,  SnmpAnnotationException;
    
}
