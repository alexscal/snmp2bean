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