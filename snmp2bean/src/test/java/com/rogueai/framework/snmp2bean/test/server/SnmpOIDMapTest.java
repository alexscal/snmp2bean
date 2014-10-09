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

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import org.junit.Assert;
import org.junit.Test;

import com.rogueai.framework.snmp2bean.helper.TestHelper;
import com.rogueai.framework.snmp2bean.test.server.SnmpOIDMap.SortedProperties;

public class SnmpOIDMapTest {
    
    @Test
    public void loadPropertiesTestSingleEntry() throws IOException {
        String propertyFileName = "SystemInfo.snmp";
        File file = TestHelper.getProjectRelativePath("snmp2bean", "src/test/resources/snmpResponses/" + propertyFileName);
        Assert.assertNotNull(file);
        
        SnmpOIDMap snmpOIDMap = new SnmpOIDMap(file.getAbsolutePath());
        String property = snmpOIDMap.getPropertyValueByIndexAndKey(0, "1.3.6.1.2.1.1.1");
        Assert.assertNotNull(property);
        
        Assert.assertEquals("SystemDescription", property);
        
    }
    
    @Test
    public void loadPropertiesTestMultipleEntries() throws IOException {
        String propertyFileName = "IfEntry.snmp";
        File file = TestHelper.getProjectRelativePath("snmp2bean", "src/test/resources/snmpResponses/" + propertyFileName);
        Assert.assertNotNull(file);
        
        SnmpOIDMap snmpOIDMap = new SnmpOIDMap(file.getAbsolutePath());
        String property = snmpOIDMap.getPropertyValueByIndexAndKey(1, "1.3.6.1.2.1.2.2.1.2");
        Assert.assertNotNull(property);
        
        Assert.assertEquals("lo", property);
        
        property = snmpOIDMap.getPropertyValueByIndexAndKey(2, "1.3.6.1.2.1.2.2.1.2");
        Assert.assertNotNull(property);
        
        Assert.assertEquals("eth0", property);
        
    }
    
    @Test
    public void sortProperties() {
        SortedProperties props = new SortedProperties();
        String propertyFileName = "SortPropertiesTest.snmp";
        File file = TestHelper.getProjectRelativePath("snmp2bean", "src/test/resources/" + propertyFileName);
        Assert.assertNotNull(file);
        
        InputStream configInputStream;
        try {
            configInputStream = new FileInputStream(file.getAbsolutePath());
            props.load(configInputStream);
        } catch (IOException e1) {
            fail();
        }
        
        Enumeration<Object> keys = props.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            System.out.println(key + " -- " + props.getProperty(key));
        }
    }
}