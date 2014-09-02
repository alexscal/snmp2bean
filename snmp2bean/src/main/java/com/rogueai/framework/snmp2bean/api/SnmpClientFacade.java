package com.rogueai.framework.snmp2bean.api;

public interface SnmpClientFacade {
    
    SnmpSessionFactory getSnmpSessionFactory();
    
    SnmpTargetFactory getSnmpTargetFactory();
    
}

