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
package com.rogueai.framework.snmp2bean.api.snmp4J.impl.dummy;

import org.snmp4j.PDU;
import org.snmp4j.Target;
import org.snmp4j.event.ResponseEvent;

import com.rogueai.framework.snmp2bean.api.snmp4J.impl.Snmp4JWrapper;
import com.rogueai.framework.snmp2bean.test.server.SnmpOIDMap;

public class Snmp4JWrapperDummyImpl implements Snmp4JWrapper {
    
    final private SnmpOIDMap snmpOIDMap;;
    
    public Snmp4JWrapperDummyImpl(SnmpOIDMap snmpOIDMap) {
        this.snmpOIDMap = snmpOIDMap;
    }

    public <T> T getSnmp() {
        return null;
    }
    
    public ResponseEvent get(PDU request, Target target) {
        PDU response = snmpOIDMap.populatePDU(request);
        ResponseEvent event = new ResponseEvent(this, null, request, response, null);
        return event;
    }
    
    public ResponseEvent getNext(PDU request, Target target) {
        PDU response = snmpOIDMap.populatePDU(request);
        ResponseEvent event = new ResponseEvent(this, null, request, response, null);
        return event;
    }
    
    public void close() {
        // TODO Auto-generated method stub
        
    }
    
    public ResponseEvent set(PDU pdu, Target target) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
