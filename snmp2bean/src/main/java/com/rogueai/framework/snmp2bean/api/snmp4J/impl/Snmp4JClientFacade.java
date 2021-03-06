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

import com.rogueai.framework.snmp2bean.api.SnmpClientFacade;
import com.rogueai.framework.snmp2bean.api.SnmpService;
import com.rogueai.framework.snmp2bean.api.SnmpServiceWrite;
import com.rogueai.framework.snmp2bean.api.SnmpSession;
import com.rogueai.framework.snmp2bean.api.SnmpSessionFactory;
import com.rogueai.framework.snmp2bean.api.SnmpTargetFactory;
import com.rogueai.framework.snmp2bean.api.snmp4J.impl.factory.Snmp4JSessionFactory;
import com.rogueai.framework.snmp2bean.api.snmp4J.impl.factory.Snmp4JSimulationSessionFactory;
import com.rogueai.framework.snmp2bean.api.snmp4J.impl.factory.Snmp4JTargetFactory;

public class Snmp4JClientFacade implements SnmpClientFacade {
    
    private SnmpSessionFactory snmp4JSessionFactory = null;
    
    private Snmp4JTargetFactory snmp4JTargetFactory = null;
    
    private SnmpService snmpService = null;
    
    private SnmpServiceWrite snmpServiceWrite = null;
    
    public Snmp4JClientFacade() {}
    
    public Snmp4JClientFacade(String snmpResponse) {
        if (snmp4JSessionFactory == null && !snmpResponse.isEmpty()) {
            snmp4JSessionFactory = new Snmp4JSimulationSessionFactory(snmpResponse);
        }
    }
    
    public SnmpServiceWrite getSnmpServiceWrite(SnmpSession snmpSession) {
        return snmpServiceWrite == null ? new Snmp4JServiceWrite(snmpSession) : snmpServiceWrite;
    }
    
    public SnmpService getSnmpService(SnmpSession snmpSession) {
        return snmpService == null ? new Snmp4JService(snmpSession) : snmpService;
    }
    
    public SnmpSessionFactory getSnmpSessionFactory() {
        return snmp4JSessionFactory == null ? new Snmp4JSessionFactory() : snmp4JSessionFactory;
    }
    
    public SnmpTargetFactory getSnmpTargetFactory() {
        return snmp4JTargetFactory == null ? new Snmp4JTargetFactory() : snmp4JTargetFactory;
    }
    
}
