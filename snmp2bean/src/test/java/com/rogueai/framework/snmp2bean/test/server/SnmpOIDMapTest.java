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

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.rogueai.framework.snmp2bean.helper.TestHelper;

public class SnmpOIDMapTest {
    
    @Test
    public void loadPropertiesTest() throws IOException {
        String propertyFileName = "SnmpServerTestResponses.properties";
        File file = TestHelper.getProjectRelativePath("snmp2bean", "src/test/resources/" + propertyFileName);
        Assert.assertNotNull(file);
        
        SnmpOIDMap snmpOIDMap = new SnmpOIDMap(file.getAbsolutePath());
        String property = snmpOIDMap.getPropertyValueByKey("1.3.6.1.2.1.1.1.0");
        Assert.assertNotNull(property);
        
        Assert.assertEquals("SystemDescription", property);
        
    }
}