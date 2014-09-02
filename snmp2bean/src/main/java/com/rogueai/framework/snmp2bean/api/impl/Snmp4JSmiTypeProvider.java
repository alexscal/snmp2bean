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
package com.rogueai.framework.snmp2bean.api.impl;

import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Counter64;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UnsignedInteger32;

import com.rogueai.framework.snmp2bean.api.SmiTypeProvider;
import com.rogueai.framework.snmp2bean.enums.SmiType;
import com.rogueai.framework.snmp2bean.exception.AssertFailureException;

public class Snmp4JSmiTypeProvider implements SmiTypeProvider {
    
    @SuppressWarnings("rawtypes")
    public Class getSmiType(SmiType smiTypeEnum) {
        switch(smiTypeEnum) {
            case INTEGER:
            case INTEGER32:
                return Integer32.class;
            case UNSIGNED32:
                return UnsignedInteger32.class;
            case COUNTER32:
                return Counter32.class;
            case GAUGE32:
                return Gauge32.class;
            case TIMETICKS:
                return TimeTicks.class;
            case COUNTER64:
                return Counter64.class;
            case OCTET_STRING:
            case OPAQUE:
                return OctetString.class;
            case OID:
                return OID.class;
            case IPADDRESS:
                return IpAddress.class;
        }
        throw new AssertFailureException("Unknow enum type: " + smiTypeEnum);
    }
}

