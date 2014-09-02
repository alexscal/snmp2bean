/*******************************************************************************
 * Copyright 2014 Rogueai.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.rogueai.framework.snmp2bean.api.snmp4J.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

import com.rogueai.framework.snmp2bean.api.SnmpTrapMsg;

public class Snmp4JTrapMsg implements SnmpTrapMsg {
    
    private Address peerAddr;

    private Map<String, Object> valueMap = new HashMap<String, Object>(5);

    public Snmp4JTrapMsg(Address address, PDU pdu) {
        this.peerAddr = address;
        valueMap.put(MSG_TYPE, "trap");
        valueMap.put(NODE_ADDR, getPeerAddr());
        valueMap.put(RECEIVED_TIME, System.currentTimeMillis());
        if (pdu instanceof PDUv1) {// v1 trap
            parseV1((PDUv1) pdu);
        } else { // v2c trap
            parseV2c(pdu);
        }
    }

    public Map<String, Object> toMap() {
        return Collections.unmodifiableMap(valueMap);
    }

    public String getTrapOid() {
        return (String) valueMap.get(TRAP_OID);
    }

    public String getPeerAddr() {
        String s = peerAddr.toString();
        int idx = s.indexOf('/');
        if(idx != -1) {
            s = s.substring(0, idx);
        }
        return s;
    }
    
    private void parseV1(PDUv1 pdu) {
        // parse headers
        valueMap.put(ENTERPRISE, pdu.getEnterprise().toString());
        valueMap.put(AGENT_ADDR, pdu.getAgentAddress());
        valueMap.put(GENERIC_TRAP, pdu.getGenericTrap());
        valueMap.put(SPECIFIC_TRAP, pdu.getSpecificTrap());
        valueMap.put(TIME_STAMP, pdu.getTimestamp());
        OID trapOid = SnmpConstants.getTrapOID(pdu.getEnterprise(), pdu
                .getGenericTrap(), pdu.getSpecificTrap());
        valueMap.put(TRAP_OID, trapOid.toString());
        // parse variable bindings
        Vector vbs = pdu.getVariableBindings();
        for (Iterator it = vbs.iterator(); it.hasNext();) {
            VariableBinding vb = (VariableBinding) it.next();
            OID oid = vb.getOid();
            Variable var = vb.getVariable();
            valueMap.put(oid.toString(), ((OID) var).getValue());
        }
    }

    private void parseV2c(PDU pdu) {
        //set the agent address the same to peer address
        valueMap.put(AGENT_ADDR, getPeerAddr());
        // parse header
        Vector vbs = pdu.getVariableBindings();
        for (Iterator it = vbs.iterator(); it.hasNext();) {
            VariableBinding vb = (VariableBinding) it.next();
            OID oid = vb.getOid();
            Variable var = vb.getVariable();
            if (oid.equals(SnmpConstants.snmpTrapOID)) {
                valueMap.put(TRAP_OID, ((OID) var).getValue().toString());
            } else if (oid.equals(SnmpConstants.sysUpTime)) {
                valueMap.put(TIME_STAMP, ((OID) var).getValue());
            } else {
                valueMap.put(oid.toString(), ((OID) var).getValue());
            }
        }
    }

    @Override
    public String toString() {
        return valueMap.toString();
    }
    
}
