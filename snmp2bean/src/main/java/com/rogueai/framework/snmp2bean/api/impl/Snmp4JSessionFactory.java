package com.rogueai.framework.snmp2bean.api.impl;

public class Snmp4JSessionFactory {
    
    private final Snmp4JSmiTypeProvider typeProvider;

    private final Snmp4JErrorMsgProvider errorMsgProvider;

    public Snmp4JSessionFactory() {
        typeProvider = new Snmp4JSmiTypeProvider();
        errorMsgProvider = new Snmp4JErrorMsgProvider();
    }

    public ISnmpSession newSnmpSession(ISnmpTarget target) throws IOException {
        Snmp4JSession session = new Snmp4JSession(target);
        session.setSmiTypeProvider(typeProvider);
        session.setSnmpErrorMsgProvider(errorMsgProvider);
        return session;
    }
    
}
