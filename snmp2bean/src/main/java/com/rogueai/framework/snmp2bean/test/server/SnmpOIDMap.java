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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

public class SnmpOIDMap {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SnmpOIDMap.class);
    
    private SortedProperties properties = new SortedProperties();
    private Map<Integer, Map<String, Object>> mapResponses = new TreeMap<>();
    private Queue<Integer> queue;
    
    public SnmpOIDMap(String propertyFilePath) throws IOException {
        loadProperties(propertyFilePath);
    }
    
    public SnmpOIDMap() {
        setUpMockData(null);
    }
    
    private void setUpMockData(Map<Integer, Map<String, Object>> mockMapResponses) {
        queue = new ConcurrentLinkedQueue<>();
        
        if (mockMapResponses == null) return;
        
        mapResponses = new TreeMap<>(mockMapResponses);
        
        for (Integer key : mapResponses.keySet()) {
            queue.add(key);
        }
    }
    
    public PDU populatePDU(PDU request) {
        PDU response = new PDU(request);
        response.setType(PDU.RESPONSE);
        
        response.clear(); // remove all the varbinds
        
        Integer index = queue.poll();
        if (index == null) {
            VariableBinding vb = new VariableBinding();
            String oid = "6.6.6.6.6.6.6.6.6.6"; // Dummy OID to stop the count
            vb.setOid(new OID(oid));
            response.add(vb);
            return response;
        }
        
        Map<String, Object> map = mapResponses.get(index);
        if (map == null)
            return null;
        
        
        Map<String, Object> sortedMap = new TreeMap<>(new AlphanumComparator());
        sortedMap.putAll(map);
        
        for (String key : sortedMap.keySet()) {
            VariableBinding vb = new VariableBinding();
            String oid = key + "." + index;
            vb.setOid(new OID(oid));
            
            Object value = sortedMap.get(key);
            Variable v = null;
            if (value instanceof Integer)
                v = new Integer32((int) value);
            else 
                v = new OctetString((String) value);
            vb.setVariable(v);
            response.add(vb);
        }
        
        return response;
    }
    
    
    
    private Properties loadProperties(String propertyFilePath) throws IOException {
        InputStream configInputStream = new FileInputStream(propertyFilePath);
        properties.load(configInputStream);
        
        mapResponses.clear();
        
        Map<Integer, Map<String, Object>> mockMapResponses = new HashMap<>();
        
        
        Enumeration<Object> keys = properties.keys();
        
        
        while (keys.hasMoreElements()) {
            String key =   (String)keys.nextElement();  
            String value = properties.getProperty(key);
            
            if (value == null || value.isEmpty()) {
                LOGGER.warn("No value returned for key [" + key + "]");
                continue;
            }
            
            String[] valueSplit = value.split(":");
            if (valueSplit == null || valueSplit.length < 2) {
                LOGGER.warn("No values from [" + value + "]");
                continue;
            }
            
            String type = valueSplit[0];
            if (type == null || type.isEmpty()) {
                LOGGER.warn("No type found in string: [" + value + "]");
                continue;
            }
            
            String stringValue = valueSplit[1];
            if (stringValue == null || stringValue.isEmpty()) {
                LOGGER.warn("No value found in string: [" + value + "]");
                continue;
            }
            
            String[] keySplit = ((String)key).split("#");
            if (keySplit == null || keySplit.length < 2) {
                LOGGER.warn("No key found in string: [" + key + "]");
                continue;
            }
            
            Integer index = Integer.valueOf(keySplit[0]);
            if (index == null) {
                LOGGER.warn("No index found in string: [" + key + "]");
                continue;
            }
            
            String oid = keySplit[1];
            if (oid == null || oid.isEmpty()) {
                LOGGER.warn("No oid found in string: [" + key + "]");
                continue;
            }
            
            Object finalValue = getValueObjectByType(stringValue, type);
            
            Map<String, Object> map;
            
            if (mockMapResponses.containsKey(index)) {
                map = mockMapResponses.get(index);
                
                if (map == null)
                    map = new TreeMap<>();
                else {
                    if (map.containsKey(oid))
                        continue;
                    
                    map.put(oid, finalValue);
                }
                
            } else {
                map = new TreeMap<>();
                map.put(oid, finalValue);
                mockMapResponses.put(index, map);
            }
        }
        
        setUpMockData(mockMapResponses);
        
        return properties;
    }
    
    private Object getValueObjectByType(String value, String type) {
        Object returnValue = null;
        
        switch (type.toUpperCase()) {
            case "INTEGER":
            case "INTEGER32":
                returnValue = Integer.valueOf(value);
                break;
            case "TIMETICKS":
            case "UNSIGNED32":
            case "COUNTER32":
            case "COUNTER64":
            case "GAUGE32":
                returnValue = Long.valueOf(value);
                break;
            case "OCTET_STRING":
                returnValue = value.getBytes();
                break;
            default:
                returnValue = value;
        }
        
        return returnValue;
    }
    
    public String getPropertyValueByIndexAndKey(Integer index, String key) {
        Map<String, Object> map = mapResponses.get(index);
        
        if (map == null)
            return null;
        
        Object object = map.get(key);
        
        return String.valueOf(object);
    }
    
    public static class SortedProperties extends Properties {
        
        private static final long serialVersionUID = -8993685038288528211L;
        
        public Enumeration<Object> keys() {
            Enumeration<Object> keysEnum = super.keys();
            Vector<Object> keyList = new Vector<Object>();
            
            while (keysEnum.hasMoreElements()) {
                keyList.add(keysEnum.nextElement());
            }
            
            Collections.sort(keyList, new AlphanumComparator());
            
            return keyList.elements();
        }
    }
}
