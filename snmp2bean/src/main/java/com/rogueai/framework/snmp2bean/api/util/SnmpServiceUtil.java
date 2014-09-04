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
package com.rogueai.framework.snmp2bean.api.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.rogueai.framework.snmp2bean.annotation.MibIndex;
import com.rogueai.framework.snmp2bean.annotation.MibObjectType;
import com.rogueai.framework.snmp2bean.annotation.MibObjectType.Access;

public class SnmpServiceUtil {
    
    public static String intAry2Str(int[] oidValue) {
        StringBuffer sb = new StringBuffer();
        for (int i : oidValue) {
            sb.append(i).append('.');
        }
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
    public static byte[] copyBytes(int[] srcArray, int srcPos, byte[] destBytes) {
        for (int k = 0; k < destBytes.length; k++) {
            destBytes[k] = (byte) (srcArray[srcPos + k] & 0xff);
        }
        return destBytes;
    }
    
    public static Field[] getPropFields(Class<?> clazz) {
        List<Field> list = new ArrayList<Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(MibObjectType.class)
                    && !field.isAnnotationPresent(MibIndex.class)) {
                list.add(field);
            }
        }
        return (Field[]) list.toArray(new Field[list.size()]);
    }
    
    public static Field[] getWritePropFields(Class<?> clazz) {
        List<Field> list = new ArrayList<Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(MibObjectType.class)
                    && !field.isAnnotationPresent(MibIndex.class)) {
                MibObjectType mot = field.getAnnotation(MibObjectType.class);
                if (mot.access() == Access.WRITE || mot.access() == Access.CREATE) {
                    list.add(field);
                }
            }
        }
        return (Field[]) list.toArray(new Field[list.size()]);
    }
    
    public static Field[] getIndexFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> list = new ArrayList<Field>();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isAnnotationPresent(MibIndex.class)) {
                fields[i].setAccessible(true);
                list.add(fields[i]);
            }
        }
        Field[] indexFields = list.toArray(new Field[list.size()]);
        // bubble sort.
        for (int i = 0; i < indexFields.length; i++) {
            for (int j = i + 1; j < indexFields.length; j++) {
                Field top = indexFields[i];
                int no = top.getAnnotation(MibIndex.class).no();
                Field another = indexFields[j];
                int anotherNo = another.getAnnotation(MibIndex.class).no();
                if (anotherNo < no) {
                    indexFields[i] = another;
                    indexFields[j] = top;
                }
            }
        }
        return indexFields;
    }
}