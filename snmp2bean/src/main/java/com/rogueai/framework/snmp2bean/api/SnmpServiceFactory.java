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
