package com.rogueai.framework.snmp2bean.api;

public interface SnmpTrapHandler {
    
    boolean onTrapMsg(SnmpTrapMsg trapMsg);
    
}
