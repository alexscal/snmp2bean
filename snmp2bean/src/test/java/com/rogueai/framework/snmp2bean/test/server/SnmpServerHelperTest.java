/*******************************************************************************
 * Copyright 2014 Rogueai.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.rogueai.framework.snmp2bean.test.server;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.snmp4j.PDU;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

import com.rogueai.framework.snmp2bean.api.SnmpTarget;
import com.rogueai.framework.snmp2bean.api.snmp4J.impl.Snmp4JTarget;

public class SnmpServerHelperTest {
    
    @Test
    public void convertBytesToPDU() throws IOException {
        
        SnmpServerHelper snmpServerHelper = new SnmpServerHelper();
        
        byte[] bytes = new byte[] {48, 38, 2, 1, 1, 4, 6, 112, 117, 98, 108, 105, 99, -96, 25, 2, 1, 0, 2, 1, 0, 2, 1, 0, 48, 14, 48, 12, 6, 4, 42, 3, 5, 6, 4, 4, 65, 108, 101, 120};
       
        PDU v2cPDU = snmpServerHelper.getPDUFromRequest(bytes);
        Assert.assertNotNull(v2cPDU);
        
        VariableBinding vb = v2cPDU.get(0);
        Assert.assertNotNull(vb);
        
        Variable variable = vb.getVariable();
        Assert.assertNotNull(variable);
        
        OID oid = vb.getOid();
        Assert.assertNotNull(oid);
        
        System.out.println("OID = " + oid.toDottedString() + " : "+ variable.toString());
        
        Assert.assertEquals("1.2.3.5.6", oid.toDottedString());
        Assert.assertEquals("Alex", variable.toString());
    }
    
    @Test
    public void buildBytesFromPDU() throws IOException {
        
        SnmpServerHelper snmpServerHelper = new SnmpServerHelper();
        
        PDU pdu = new PDU();
        pdu.setType(PDU.GET);
        OID oid = new OID("1.2.3.5.6");
        VariableBinding vb = new VariableBinding(oid);
        Variable v = new OctetString("Alex");
        vb.setVariable(v);
        pdu.add(vb);
        
        SnmpTarget target = new Snmp4JTarget("");
        
       byte[] messageBytes = snmpServerHelper.getBytesMessageFromPDU(pdu, target);
       
       Assert.assertArrayEquals(new byte[]{48, 38, 2, 1, 1, 4, 6, 112, 117, 98, 108, 105, 99, -96, 25, 2, 1, 0, 2, 1, 0, 2, 1, 0, 48, 14, 48, 12, 6, 4, 42, 3, 5, 6, 4, 4, 65, 108, 101, 120}, messageBytes);
        
    }
}