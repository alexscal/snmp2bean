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

import org.snmp4j.CommunityTarget;
import org.snmp4j.Target;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;

import com.rogueai.framework.snmp2bean.api.SnmpTarget;

public class Snmp4JTarget implements SnmpTarget {
    
    private int port = 161;
    
    private String ip;
    
    private int version = V2C;
    
    private String community = "public";
    
    private String writeCommunity = "private";
    
    public Snmp4JTarget(String ip) {
        this.ip = ip;
    }
    
    public Snmp4JTarget(String ip, int port) {
        this(ip);
        this.port = port;
    }
    
    public Target getReadTarget() {
        return getTarget(community);
    }
    
    public Target getWriteTarget() {
        return getTarget(writeCommunity);
    }
    
    public String getCommunity() {
        return community;
    }
    
    public void setCommunity(String community) {
        this.community = community;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public int getPort() {
        return port;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public int getVersion() {
        return version;
    }
    
    public void setVersion(int version) {
        this.version = version;
    }
    
    public String getWriteCommunity() {
        return writeCommunity;
    }
    
    public void setWriteCommunity(String writeCommunity) {
        this.writeCommunity = writeCommunity;
    }
    
    private Target getTarget(String communityString) {
        if(version == V1 || version == V2C) {
            Address address = GenericAddress.parse("udp:" + ip + "/" + port);
            CommunityTarget target = new CommunityTarget(address, new OctetString(communityString));
            target.setVersion(version);
            return target;
        }
        throw new RuntimeException("Snmpv3 is not supported");       
    }
}