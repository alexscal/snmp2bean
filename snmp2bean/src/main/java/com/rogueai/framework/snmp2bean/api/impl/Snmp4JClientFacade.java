package com.rogueai.framework.snmp2bean.api.impl;

import com.rogueai.framework.snmp2bean.api.SnmpClientFacade;

public class Snmp4JClientFacade implements SnmpClientFacade {

    private Snmp4JSessionFactory snmp4JSessionFactory = null;
    
    private Snmp4JTargetFactory snmp4JTargetFactory = null;
    
    public ISnmpSessionFactory getSnmpSessionFactory() {
        if(snmp4JSessionFactory == null) {
            snmp4JSessionFactory = new Snmp4JSessionFactory();
        }
        return snmp4JSessionFactory;
    }

    public ISnmpTargetFactory getSnmpTargetFactory() {
        if(snmp4JTargetFactory == null) {
            snmp4JTargetFactory = new Snmp4JTargetFactory();
        }
        return snmp4JTargetFactory;
    }

}
