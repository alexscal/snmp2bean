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
package com.rogueai.framework.snmp2bean.api.snmp4J.impl.dummy;

import java.io.IOException;

import org.snmp4j.Target;

import com.rogueai.framework.snmp2bean.api.AbstractSnmpSession;
import com.rogueai.framework.snmp2bean.api.snmp4J.impl.Snmp4JWrapper;
import com.rogueai.framework.snmp2bean.test.server.SnmpOIDMap;

public class Snmp4JDummySession extends AbstractSnmpSession {

    final private Snmp4JWrapper snmp4JWrapper; 
    
    public Snmp4JDummySession(SnmpOIDMap snmpOIDMap) {
        snmp4JWrapper = new Snmp4JWrapperDummyImpl(snmpOIDMap);
    }
    
    @Override
    public Snmp4JWrapper getSnmpWrapper() {
       return snmp4JWrapper;
    }

    @Override
    public Target getReadTarget() {
        return null;
    }

    @Override
    public Target getWriteTarget() {
        return null;
    }

    @Override
    public void close() throws IOException {
        
    }
    
}
