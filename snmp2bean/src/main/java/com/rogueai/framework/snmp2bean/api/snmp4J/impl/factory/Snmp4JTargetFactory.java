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
package com.rogueai.framework.snmp2bean.api.snmp4J.impl.factory;

import com.rogueai.framework.snmp2bean.api.SnmpTarget;
import com.rogueai.framework.snmp2bean.api.SnmpTargetFactory;
import com.rogueai.framework.snmp2bean.api.snmp4J.impl.Snmp4JTarget;

public class Snmp4JTargetFactory implements SnmpTargetFactory {
  
    public Snmp4JTargetFactory() {
    }

    public SnmpTarget createSnmpTarget(String ip) {
        return new Snmp4JTarget(ip);
    }

    public SnmpTarget createSnmpTarget(String ip, Integer port) {
        return port != null ? new Snmp4JTarget(ip, port) : createSnmpTarget(ip);
    }
    
}
