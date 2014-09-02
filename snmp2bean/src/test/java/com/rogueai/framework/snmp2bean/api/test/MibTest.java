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
package com.rogueai.framework.snmp2bean.api.test;

import junit.framework.TestCase;

import com.rogueai.framework.snmp2bean.api.SnmpClientFacade;
import com.rogueai.framework.snmp2bean.api.SnmpService;
import com.rogueai.framework.snmp2bean.api.SnmpSession;
import com.rogueai.framework.snmp2bean.api.SnmpSessionFactory;
import com.rogueai.framework.snmp2bean.api.SnmpTargetFactory;
import com.rogueai.framework.snmp2bean.api.snmp4J.impl.Snmp4JClientFacade;
import com.rogueai.framework.snmp2bean.api.snmp4J.impl.Snmp4JService;

public class MibTest extends TestCase {
    
    protected SnmpSession session;
    
    protected SnmpService service;

    @Override
    protected void setUp() throws Exception {
        SnmpClientFacade facade = new Snmp4JClientFacade();
        SnmpSessionFactory sessionFactory = facade.getSnmpSessionFactory();
        SnmpTargetFactory targetFactory = facade.getSnmpTargetFactory();
        session = sessionFactory.newSnmpSession(targetFactory.newSnmpTarget("127.0.0.1", 161));
        service = new Snmp4JService();
        ((Snmp4JService)service).setSnmpSession(session);
        
    }
    
    @Override
    protected void tearDown() throws Exception {
        session.close();
    }
    
}
