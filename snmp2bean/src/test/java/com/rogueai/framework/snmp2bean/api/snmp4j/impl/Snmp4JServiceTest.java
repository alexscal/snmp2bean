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
package com.rogueai.framework.snmp2bean.api.snmp4j.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.rogueai.framework.snmp2bean.api.SnmpService;
import com.rogueai.framework.snmp2bean.api.SnmpServiceFactory;
import com.rogueai.framework.snmp2bean.api.snmp4J.impl.Snmp4JClientFacade;
import com.rogueai.framework.snmp2bean.domain.test.IfEntry;
import com.rogueai.framework.snmp2bean.domain.test.MultipleIndexEntry;
import com.rogueai.framework.snmp2bean.domain.test.SystemInfo;
import com.rogueai.framework.snmp2bean.helper.TestHelper;

public class Snmp4JServiceTest  {
    
    public SnmpService buildService(String snmpResponse) {
        File file = TestHelper.getProjectRelativePath("snmp2bean", "src/test/resources/snmpResponses/" + snmpResponse);
        Snmp4JClientFacade snmpClientFacade = new Snmp4JClientFacade(file.getAbsolutePath());
        SnmpServiceFactory snmpServiceFactory = new SnmpServiceFactory(snmpClientFacade);
        SnmpService service = snmpServiceFactory.builSnmpService("127.0.0.1", 161, "public");
        return service;
    }
    
    @Test
    public void SingleIndexTableTest() {
        SnmpService service = buildService("IfEntry.snmp");
        try {
            List<IfEntry> list = printResultsForGetTable(service, IfEntry.class);
            assertTrue(list.size() > 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    @Test    
    public void testGet() {
        SnmpService service = buildService("SystemInfo.snmp");
        try {
            SystemInfo sysMIB = service.get(SystemInfo.class);
            assertNotNull(sysMIB.getSysContact());
            assertNotNull(sysMIB.getSysDesc());
            System.out.println(sysMIB.getSysContact());
            System.out.println(sysMIB.getSysDesc());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    @Test
    public void MultipleIndexEntryTest() {
        SnmpService service = buildService("MultipleIndexEntry");
        try {
            printResultsForGetTable(service, MultipleIndexEntry.class);
            
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    protected <T> List<T> printResultsForGetTable(SnmpService service, Class<T> classz)
    {
        try
        {
            List<T> list = service.getTable(classz);
            if (list.size() == 0)
                return Collections.emptyList();
            for (T t : list)
            {
                TestHelper.printBean(t);
                return list;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail();
        }
        return Collections.emptyList();
    }

    protected <T> T printResultsForGet(SnmpService service, Class<T> classz)
    {
        try
        {
            T t = service.get(classz);
            TestHelper.printBean(t);
            return t;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail();
        }
        return null;
    }
    
}
