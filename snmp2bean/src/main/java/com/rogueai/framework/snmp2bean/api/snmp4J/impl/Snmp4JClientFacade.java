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
import com.rogueai.framework.snmp2bean.api.SnmpSessionFactory;
import com.rogueai.framework.snmp2bean.api.SnmpTargetFactory;

public class Snmp4JClientFacade implements SnmpClientFacade {
    
    private SnmpSessionFactory snmp4JSessionFactory = null;
    
    private Snmp4JTargetFactory snmp4JTargetFactory = null;
    
    public SnmpService getSnmpServiceFactory() {
        return null;
    }
    
    public SnmpSessionFactory getSnmpSessionFactory() {
        if(snmp4JSessionFactory == null) {
            snmp4JSessionFactory = new Snmp4JSessionFactory();
        }
        return snmp4JSessionFactory;
    }
    
    public SnmpTargetFactory getSnmpTargetFactory() {
        if(snmp4JTargetFactory == null) {
            snmp4JTargetFactory = new Snmp4JTargetFactory();
        }
        return snmp4JTargetFactory;
    }
    
}
