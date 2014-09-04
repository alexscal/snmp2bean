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
package com.rogueai.framework.snmp2bean.api.snmp4j.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import com.rogueai.framework.snmp2bean.api.test.InitSnmpTest;
import com.rogueai.framework.snmp2bean.domain.test.IfEntry;
import com.rogueai.framework.snmp2bean.domain.test.SystemInfo;


public class Snmp4jSessionFactoryTest extends InitSnmpTest {
    
    @Test    
    public void testGet() {
        try {
            SystemInfo sysMIB = service.get(SystemInfo.class);
            assertNotNull(sysMIB.getSysObjectID());
            assertEquals("Alessandro Scanu <alessandro.scanu@vce.com>", sysMIB.getSysContact());            
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    @Test
    public void testGetByIndex() {
        try {
            List<IfEntry> list = service.getTable(IfEntry.class);
            assertTrue(list.size() > 0);
            IfEntry rtIfEntry = (IfEntry) service.getByIndex(IfEntry.class, list.get(0).getIfIndex());
            assertNotNull(rtIfEntry.getIfDescr());
            rtIfEntry = (IfEntry) service.getByIndex(IfEntry.class, 1,
                    new String[] { "ifDescr" });
            assertNotNull(rtIfEntry.getIfDescr());
            assertTrue(rtIfEntry.getIfInDiscards() == 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
    @Test
    public void testGetTable() {
        try {
            List<IfEntry> list = service.getTable(IfEntry.class);
            assertTrue(list.size() > 0);
            for (Object o : list) {
                System.out.println(((IfEntry) o).getIfDescr());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}


