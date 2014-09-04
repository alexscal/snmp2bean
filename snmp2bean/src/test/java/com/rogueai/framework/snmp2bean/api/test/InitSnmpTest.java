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

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.rogueai.framework.snmp2bean.api.SnmpService;
import com.rogueai.framework.snmp2bean.api.SnmpServiceFactory;
import com.rogueai.framework.snmp2bean.api.snmp4J.impl.Snmp4JClientFacade;


public abstract class InitSnmpTest {
    
    public static SnmpService service;

    @BeforeClass
    public static void setUp() throws Exception {
        SnmpServiceFactory snmpServiceFactory = new SnmpServiceFactory(new Snmp4JClientFacade());
        service = snmpServiceFactory.builSnmpService("127.0.0.1", 161, "public");
    }
    
    @AfterClass
    public static void tearDown() throws Exception {
        service.close();
    }
    
}
