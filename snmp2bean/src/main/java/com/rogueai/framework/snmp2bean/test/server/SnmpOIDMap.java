package com.rogueai.framework.snmp2bean.test.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SnmpOIDMap {
    
    private Properties properties = new Properties();
    
    public SnmpOIDMap() {}
    
    public SnmpOIDMap(String propertyFilePath) throws IOException {
        loadProperties(propertyFilePath);
    }
    
    private Properties loadProperties(String propertyFilePath) throws IOException {
        InputStream configInputStream = new FileInputStream(propertyFilePath);
        properties.load(configInputStream);
        return properties;
    }
    
    public String getPropertyValueByKey(String key) {
        String prefix = "oid.";
        String property = properties.getProperty(prefix + key);
        return property;
    }
    
}
