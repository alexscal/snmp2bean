package com.rogueai.framework.snmp2bean.api.impl;

import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Counter64;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UnsignedInteger32;

import com.rogueai.framework.snmp2bean.api.SmiTypeProvider;
import com.rogueai.framework.snmp2bean.enums.SmiType;
import com.rogueai.framework.snmp2bean.exception.AssertFailureException;

public class Snmp4JSmiTypeProvider implements SmiTypeProvider {
    
    @SuppressWarnings("rawtypes")
    public Class getSmiType(SmiType smiTypeEnum) {
        switch(smiTypeEnum) {
            case INTEGER:
            case INTEGER32:
                return Integer32.class;
            case UNSIGNED32:
                return UnsignedInteger32.class;
            case COUNTER32:
                return Counter32.class;
            case GAUGE32:
                return Gauge32.class;
            case TIMETICKS:
                return TimeTicks.class;
            case COUNTER64:
                return Counter64.class;
            case OCTET_STRING:
            case OPAQUE:
                return OctetString.class;
            case OID:
                return OID.class;
            case IPADDRESS:
                return IpAddress.class;
        }
        throw new AssertFailureException("Unknow enum type: " + smiTypeEnum);
    }
}

