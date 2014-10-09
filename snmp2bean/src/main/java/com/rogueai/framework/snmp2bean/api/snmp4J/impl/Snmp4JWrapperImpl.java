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

import java.io.IOException;

import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.event.ResponseEvent;


public class Snmp4JWrapperImpl<T> implements Snmp4JWrapper {
    
    private final Snmp snmp;
    
    public Snmp4JWrapperImpl(Snmp snmp) {
        this.snmp = snmp;
    }
    
    public Snmp getSnmp() {
        return snmp;
    }

    public ResponseEvent get(PDU pdu, Target target) {
        try {
            return snmp.get(pdu, target);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEvent getNext(PDU pdu, Target target) {
        try {
            return snmp.getNext(pdu, target);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            snmp.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ResponseEvent set(PDU pdu, Target target) {
        try {
            return snmp.set(pdu, target);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }    
}
