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
package com.rogueai.framework.snmp2bean.api.snmp4J.impl.factory;

import java.io.IOException;

import org.snmp4j.smi.IpAddress;

import com.rogueai.framework.snmp2bean.api.SnmpSession;
import com.rogueai.framework.snmp2bean.api.SnmpTarget;
import com.rogueai.framework.snmp2bean.api.snmp4J.impl.Snmp4JSession;
import com.rogueai.framework.snmp2bean.test.server.DummyTransport;
import com.rogueai.framework.snmp2bean.test.server.SnmpOIDMap;

public class Snmp4JSimulationSessionFactory extends Snmp4JSessionFactory {

    private String snmpResponseFile;
    
    public Snmp4JSimulationSessionFactory() {
    }
    
    public Snmp4JSimulationSessionFactory(String snmpResponseFile) {
        this.snmpResponseFile = snmpResponseFile;
    }
    
    @Override
    public SnmpSession createSnmpSession(SnmpTarget target) throws IOException {
        IpAddress ipAddress = new IpAddress("127.0.0.1");
        DummyTransport<IpAddress> transportMapping = new DummyTransport<IpAddress>(ipAddress);
        
        if (snmpResponseFile != null) {
            SnmpOIDMap snmpOIDMap = new SnmpOIDMap(snmpResponseFile);
            transportMapping.setSnmpOIDMap(snmpOIDMap);
        }
        
        Snmp4JSession session = new Snmp4JSession(target, transportMapping);
        session.setSmiTypeProvider(typeProvider);
        session.setSnmpErrorMsgProvider(errorMsgProvider);
        return session;
    }
    
}
