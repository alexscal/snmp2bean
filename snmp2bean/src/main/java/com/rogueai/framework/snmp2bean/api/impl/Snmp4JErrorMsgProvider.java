package com.rogueai.framework.snmp2bean.api.impl;

import org.snmp4j.mp.SnmpConstants;

import com.rogueai.framework.snmp2bean.api.SnmpErrorMsgProvider;

public class Snmp4JErrorMsgProvider implements SnmpErrorMsgProvider {
    
    public String getErrorMsg(int errorStatus, int errorIndex) {
        if(0 <= errorStatus && errorStatus <= SnmpConstants.SNMP_ERROR_MESSAGES.length) {
            return SnmpConstants.SNMP_ERROR_MESSAGES[errorStatus];
        }
        return "";
    }
    
}
