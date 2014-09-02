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
    
    public static Field[] getPropFields(Class clazz) {
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
    
    public static Field[] getWritePropFields(Class clazz) {
        List<Field> list = new ArrayList<Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(MibObjectType.class)
                    && !field.isAnnotationPresent(MibIndex.class)) {
                MibObjectType mot = field.getAnnotation(MibObjectType.class);
                if (mot.access() == Access.WRITE
                        || mot.access() == Access.CREATE) {
                    list.add(field);
                }
            }
        }
        return (Field[]) list.toArray(new Field[list.size()]);
    }
}