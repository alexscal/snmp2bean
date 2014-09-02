package com.rogueai.framework.snmp2bean.api;

public interface SnmpTrapD {
    
    String TRAPD_ENABLE = "com.rogueai.framework.snmp2bean.trapd.enable";

    String TRAPD_PORT = "com.rogueai.framework.snmp2bean.trapd.port";

    String TRAPD_THREAD = "com.rogueai.framework.snmp2bean.trapd.thread";

    void start() throws Exception;

    void addTrapHandler(SnmpTrapHandler handler);
    
    void removeTrapHandler(SnmpTrapHandler handler);
    
    void stop() throws Exception;
    
}
