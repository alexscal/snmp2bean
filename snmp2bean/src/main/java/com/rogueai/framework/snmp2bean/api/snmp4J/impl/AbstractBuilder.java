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
package com.rogueai.framework.snmp2bean.api.snmp4J.impl;

import java.lang.reflect.Field;

import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.UnsignedInteger32;

import com.rogueai.framework.snmp2bean.annotation.MibIndex;
import com.rogueai.framework.snmp2bean.annotation.MibObjectType;
import com.rogueai.framework.snmp2bean.api.SmiTypeProvider;
import com.rogueai.framework.snmp2bean.api.util.SnmpServiceUtil;
import com.rogueai.framework.snmp2bean.enums.SmiType;

public abstract class AbstractBuilder {
    
    private SmiTypeProvider smiTypeProvider;
    
    protected OID buildIndexOID(Object entry) throws IllegalArgumentException, IllegalAccessException {
        Class clazz = entry.getClass();
        Field[] indexFields = SnmpServiceUtil.getIndexFields(clazz);
        OID oid = null;
        for (Field indexField : indexFields) {
            indexField.setAccessible(true);
            Object value = indexField.get(entry);
            MibIndex miAnnotation = indexField.getAnnotation(MibIndex.class);
            int length = miAnnotation.length();
            if (length == MibIndex.VARSTR_WITH_LENGTH) {
                byte[] bytes = ((String) value).getBytes();
                int[] integers = new int[bytes.length + 1];
                integers[0] = bytes.length;
                int i = 1;
                for (byte b : bytes) {
                    integers[i++] = b;
                }
                oid = appendRawOids(oid, integers);
            } else if (length == MibIndex.VARSTR_WITHOUT_LENGTH) {
                byte[] bytes = ((String) value).getBytes();
                int[] integers = new int[bytes.length];
                int i = 0;
                for (byte b : bytes) {
                    integers[i++] = b;
                }
                oid = appendRawOids(oid, integers);
            } else if (length == 1) {
                MibObjectType mot = indexField.getAnnotation(MibObjectType.class);
                Class<?> smiTypeClass = smiTypeProvider.getSmiType(mot.smiType());
                if (smiTypeClass.equals(Integer32.class)) {
                    int v = ((Integer) value).intValue();
                    oid = appendRawOids(oid, new int[] { v });
                } else if (UnsignedInteger32.class.isAssignableFrom(smiTypeClass)) {
                    int v = (int) ((Long) value).longValue();
                    oid = appendRawOids(oid, new int[] { v });
                } else {
                    throw new RuntimeException("Index length should not be 1." + indexField);
                }
            } else if (length >= 1) {
                MibObjectType mot = indexField.getAnnotation(MibObjectType.class);
                SmiType smiType = mot.smiType();
                if (smiType == SmiType.OID) {
                    if (oid == null)
                        oid = new OID();
                    oid.append((String) value);
                } else if (smiType == SmiType.DISPLAY_STRING) {
                    byte[] bytes = ((String) value).getBytes();
                    int[] integers = new int[length];
                    for (int i = 0; i < integers.length; i++) {
                        integers[i] = (int) bytes[i];
                    }
                    oid = appendRawOids(oid, integers);
                } else if (smiType == SmiType.OCTET_STRING) {
                    byte[] bytes = ((byte[]) value);
                    int[] integers = new int[length];
                    for (int i = 0; i < integers.length; i++) {
                        integers[i] = (int) bytes[i];
                    }
                    oid = appendRawOids(oid, integers);
                } else if (smiType == SmiType.IPADDRESS) {
                    String[] strBytes = ((String) value).split("\\.");
                    if (strBytes.length != 4)
                        throw new RuntimeException("Assert faild. IpAddres length must be 1.");
                    int[] integers = new int[strBytes.length];
                    for (int i = 0; i < integers.length; i++) {
                        integers[i] = Integer.parseInt(strBytes[i]);
                    }
                    oid = appendRawOids(oid, integers);
                } else {
                    throw new RuntimeException("Unknow smiType: " + smiType);
                }
            } else {
                throw new RuntimeException("Assert Failed! Unknow index length.");
            }
        }
        return oid;
    }
    
    protected OID appendRawOids(OID oid, int[] integers) {
        if (oid == null)
            return new OID(integers);
        
        oid.append(new OID(integers));
        return oid;
    }

    public void setSmiTypeProvider(SmiTypeProvider smiTypeProvider) {
        this.smiTypeProvider = smiTypeProvider;
    }
}
