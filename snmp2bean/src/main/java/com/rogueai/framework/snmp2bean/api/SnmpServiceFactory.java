package com.rogueai.framework.snmp2bean.api;

import java.io.IOException;

public class SnmpServiceFactory {
    
    private SnmpClientFacade snmpClientFacade;
    private SnmpSession snmpSession;
    
    public SnmpServiceFactory(SnmpClientFacade snmpClientFacade) {
        this.snmpClientFacade = snmpClientFacade;
    }
    
    public SnmpService builSnmpService(String ipAddress, Integer port, String communityString) {
        
        SnmpService snmpService = null;
        
        try {
            SnmpSession snmpSession = buildSnmpSession(ipAddress, port, communityString);
            if (snmpSession == null) {
                // TODO Log and do something?
            }
            
            snmpService = snmpClientFacade.getSnmpService(snmpSession);
            
        } catch (IOException e) {
            // TODO add logging
        }
        return snmpService;
    }
    
    
    public SnmpServiceWrite builSnmpServiceWrite(String ipAddress, Integer port, String communityString) {
        SnmpServiceWrite snmpService = null;
        
        try {
            SnmpSession snmpSession = buildSnmpSession(ipAddress, port, communityString);
            if (snmpSession == null) {
                // TODO Log and do something?
            }
            
            snmpService = snmpClientFacade.getSnmpServiceWrite(snmpSession);
            
        } catch (IOException e) {
            // TODO add logging
        }
        return snmpService;
    }
    
    private SnmpSession buildSnmpSession(String ipAddress, Integer port, String communityString) throws IOException {
        SnmpSessionFactory snmpSessionFactory = snmpClientFacade.getSnmpSessionFactory();
        SnmpTargetFactory snmpTargetFactory = snmpClientFacade.getSnmpTargetFactory();
        
        SnmpTarget snmpTarget = snmpTargetFactory.createSnmpTarget(ipAddress, port);
        snmpTarget.setCommunity(communityString);
        snmpSession =  snmpSessionFactory.createSnmpSession(snmpTarget);
        return snmpSession;
    }

    public SnmpSession getSnmpSession() {
        return snmpSession;
    }
}
