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
package com.rogueai.framework.snmp2bean.api.snmp4J.impl;

import java.io.IOException;

import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.rogueai.framework.snmp2bean.api.AbstractSnmpSession;
import com.rogueai.framework.snmp2bean.api.SnmpTarget;

public class Snmp4JSession extends AbstractSnmpSession {
    
    private Snmp snmp4J;
    
    private final Snmp4JTarget target;
    
    public Snmp4JSession(SnmpTarget target) throws IOException {
        this(target, new DefaultUdpTransportMapping());
    }
    
    public Snmp4JSession(SnmpTarget target, TransportMapping transportMapping) throws IOException {
        this.target = (Snmp4JTarget) target;
        initSnmp4J(transportMapping);
    }
    
    public void initSnmp4J(TransportMapping transportMapping) throws IOException {
        if (target == null) return; // no execution.. maybe throw exception
        snmp4J = new Snmp(transportMapping);
        snmp4J.listen();
    }
    
    public Snmp4JWrapper getSnmpWrapper() {
        Snmp4JWrapper snmpWrapper = new Snmp4JWrapperImpl<Snmp>(snmp4J);
        return snmpWrapper;
    }
    
    public Target getReadTarget() {
        Target targetImpl = target.getReadTarget();
        initTimeoutRetries(targetImpl);
        return targetImpl;
    }
    
    private void initTimeoutRetries(Target targetImpl) {
        targetImpl.setTimeout(getTimeout());
        targetImpl.setRetries(getRetries());
    }
    
    public Target getWriteTarget() {
        Target targetImpl = target.getWriteTarget();
        initTimeoutRetries(targetImpl);
        return targetImpl;
    }
    
    public void close() throws IOException {
        snmp4J.close();
    }
}