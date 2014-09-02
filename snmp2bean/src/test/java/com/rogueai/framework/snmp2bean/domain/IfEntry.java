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
package com.rogueai.framework.snmp2bean.domain;

import java.io.Serializable;
import java.util.HashMap;

import com.rogueai.framework.snmp2bean.annotation.MibIndex;
import com.rogueai.framework.snmp2bean.annotation.MibObjectType;
import com.rogueai.framework.snmp2bean.annotation.MibObjectType.Access;
import com.rogueai.framework.snmp2bean.annotation.MibTable;
import com.rogueai.framework.snmp2bean.enums.SmiType;

@MibTable
public class IfEntry implements Serializable {
    
    private static final long serialVersionUID = -8035944989711722426L;
    
    @MibIndex(no = 0, length = 1)
    @MibObjectType(oid = "1.3.6.1.2.1.2.2.1.1", smiType = SmiType.INTEGER32, access = Access.READ) 
    private int ifIndex;
    
    @MibObjectType(oid = "1.3.6.1.2.1.2.2.1.2", smiType = SmiType.DISPLAY_STRING, access = Access.READ) 
    private String ifDescr;
    
    @MibObjectType(oid = "1.3.6.1.2.1.2.2.1.3", smiType = SmiType.INTEGER32, access = Access.READ) 
    private int ifType;
    
    @MibObjectType(oid = "1.3.6.1.2.1.2.2.1.4", smiType = SmiType.INTEGER32, access = Access.READ) 
    private int ifMtu;
    
    @MibObjectType(oid = "1.3.6.1.2.1.2.2.1.5", smiType = SmiType.GAUGE32, access = Access.READ) 
    private long ifSpeed;
    
    @MibObjectType(oid = "1.3.6.1.2.1.2.2.1.6", smiType = SmiType.OCTET_STRING, access = Access.READ) 
    private byte[] ifPhysAddress;
    
    @MibObjectType(oid = "1.3.6.1.2.1.2.2.1.7", smiType = SmiType.INTEGER32, access = Access.READ) 
    private int ifAdminStatus;
    
    @MibObjectType(oid = "1.3.6.1.2.1.2.2.1.8", smiType = SmiType.INTEGER32, access = Access.READ) 
    private int ifOperStatus;
    
    @MibObjectType(oid = "1.3.6.1.2.1.2.2.1.9", smiType = SmiType.TIMETICKS, access = Access.READ) 
    private long ifLastChange;
    
    @MibObjectType(oid = "1.3.6.1.2.1.2.2.1.10", smiType = SmiType.COUNTER32, access = Access.READ) 
    private long ifInOctets;
    
    @MibObjectType(oid = "1.3.6.1.2.1.2.2.1.11", smiType = SmiType.COUNTER32, access = Access.READ) 
    private long ifInUcastPkts;
    
    @MibObjectType(oid = "1.3.6.1.2.1.2.2.1.12", smiType = SmiType.COUNTER32, access = Access.READ) 
    private long ifInNUcastPkts;
    
    @MibObjectType(oid = "1.3.6.1.2.1.2.2.1.13", smiType = SmiType.COUNTER32, access = Access.READ) 
    private long ifInDiscards;
    
    @MibObjectType(oid = "1.3.6.1.2.1.2.2.1.14", smiType = SmiType.COUNTER32, access = Access.READ) 
    private long ifInErrors;
    
    @MibObjectType(oid = "1.3.6.1.2.1.2.2.1.15", smiType = SmiType.COUNTER32, access = Access.READ) 
    private long ifInUnknownProtos;
    
    @MibObjectType(oid = "1.3.6.1.2.1.2.2.1.16", smiType = SmiType.COUNTER32, access = Access.READ) 
    private long ifOutOctets;
    
    @MibObjectType(oid = "1.3.6.1.2.1.2.2.1.17", smiType = SmiType.COUNTER32, access = Access.READ) 
    private long ifOutUcastPkts;
    
    @MibObjectType(oid = "1.3.6.1.2.1.2.2.1.18", smiType = SmiType.COUNTER32, access = Access.READ) 
    private long ifOutNUcastPkts;
    
    @MibObjectType(oid = "1.3.6.1.2.1.2.2.1.19", smiType = SmiType.COUNTER32, access = Access.READ) 
    private long ifOutDiscards;
    
    @MibObjectType(oid = "1.3.6.1.2.1.2.2.1.20", smiType = SmiType.COUNTER32, access = Access.READ) 
    private long ifOutErrors;
    
    public IfEntry() {}
    
    public IfEntry(int index) {
        this.ifIndex = index;
    }
    
    public boolean isLoopback() {
        return ifType == 24;
    }
    
    public int getIfAdminStatus() {
        return ifAdminStatus;
    }
    
    public void setIfAdminStatus(int ifAdminStatus) {
        this.ifAdminStatus = ifAdminStatus;
    }
    
    public String getIfDescr() {
        return ifDescr;
    }
    
    public void setIfDescr(String ifDescr) {
        this.ifDescr = ifDescr;
    }
    
    public int getIfIndex() {
        return ifIndex;
    }
    
    public void setIfIndex(int ifIndex) {
        this.ifIndex = ifIndex;
    }
    
    public long getIfInDiscards() {
        return ifInDiscards;
    }
    
    public void setIfInDiscards(long ifInDiscards) {
        this.ifInDiscards = ifInDiscards;
    }
    
    public long getIfInErrors() {
        return ifInErrors;
    }
    
    public void setIfInErrors(long ifInErrors) {
        this.ifInErrors = ifInErrors;
    }
    
    public long getIfInNUcastPkts() {
        return ifInNUcastPkts;
    }
    
    public void setIfInNUcastPkts(long ifInNUcastPkts) {
        this.ifInNUcastPkts = ifInNUcastPkts;
    }
    
    public long getIfInOctets() {
        return ifInOctets;
    }
    
    public void setIfInOctets(long ifInOctets) {
        this.ifInOctets = ifInOctets;
    }
    
    public long getIfInUcastPkts() {
        return ifInUcastPkts;
    }
    
    public void setIfInUcastPkts(long ifInUcastPkts) {
        this.ifInUcastPkts = ifInUcastPkts;
    }
    
    public long getIfInUnknownProtos() {
        return ifInUnknownProtos;
    }
    
    public void setIfInUnknownProtos(long ifInUnknownProtos) {
        this.ifInUnknownProtos = ifInUnknownProtos;
    }
    
    public long getIfLastChange() {
        return ifLastChange;
    }
    
    public void setIfLastChange(long ifLastChange) {
        this.ifLastChange = ifLastChange;
    }
    
    public int getIfMtu() {
        return ifMtu;
    }
    
    public void setIfMtu(int ifMtu) {
        this.ifMtu = ifMtu;
    }
    
    public int getIfOperStatus() {
        return ifOperStatus;
    }
    
    public void setIfOperStatus(int ifOperStatus) {
        this.ifOperStatus = ifOperStatus;
    }
    
    public long getIfOutDiscards() {
        return ifOutDiscards;
    }
    
    public void setIfOutDiscards(long ifOutDiscards) {
        this.ifOutDiscards = ifOutDiscards;
    }
    
    public long getIfOutErrors() {
        return ifOutErrors;
    }
    
    public void setIfOutErrors(long ifOutErrors) {
        this.ifOutErrors = ifOutErrors;
    }
    
    public long getIfOutNUcastPkts() {
        return ifOutNUcastPkts;
    }
    
    public void setIfOutNUcastPkts(long ifOutNUcastPkts) {
        this.ifOutNUcastPkts = ifOutNUcastPkts;
    }
    
    public long getIfOutOctets() {
        return ifOutOctets;
    }
    
    public void setIfOutOctets(long ifOutOctets) {
        this.ifOutOctets = ifOutOctets;
    }
    
    public long getIfOutUcastPkts() {
        return ifOutUcastPkts;
    }
    
    public void setIfOutUcastPkts(long ifOutUcastPkts) {
        this.ifOutUcastPkts = ifOutUcastPkts;
    }
    
    public byte[] getIfPhysAddress() {
        return ifPhysAddress;
    }
    
    public void setIfPhysAddress(byte[] ifPhysAddress) {
        this.ifPhysAddress = ifPhysAddress;
    }
    
    public long getIfSpeed() {
        return ifSpeed;
    }
    
    public void setIfSpeed(long ifSpeed) {
        this.ifSpeed = ifSpeed;
    }
    
    public int getIfType() {
        return ifType;
    }
    
    public void setIfType(int ifType) {
        this.ifType = ifType;
    }
    
    static HashMap<Integer, String> typesMap = new HashMap<Integer, String>();
    static {
        typesMap.put(1, "Other"); 
        typesMap.put(2, "Regular1822");  
        typesMap.put(3, "hdh1822"); 
        typesMap.put(4, "ddn-x25"); 
        typesMap.put(5, "rfc877-x25"); 
        typesMap.put(6, "ethernet-csmacd");  
        typesMap.put(7, "iso88023-csmacd"); 
        typesMap.put(8, "iso88024-tokenBus"); 
        typesMap.put(9, "iso88025-tokenRing"); 
        typesMap.put(10, "iso88026-man");  
        typesMap.put(11, "starLan"); 
        typesMap.put(12, "proteon-10Mbit"); 
        typesMap.put(13, "proteon-80Mbit"); 
        typesMap.put(14, "hyperchannel"); 
        typesMap.put(15, "fddi"); 
        typesMap.put(16, "lapb"); 
        typesMap.put(17, "sdlc"); 
        typesMap.put(18, "ds1"); 
        typesMap.put(19, "e1"); 
        typesMap.put(20, "basicISDN"); 
        typesMap.put(21, "primaryISDN"); 
        typesMap.put(22, "propPointToPointSerial"); 
        typesMap.put(23, "ppp"); 
        typesMap.put(24, "softwareLoopback");  
        typesMap.put(25, "eon"); 
        typesMap.put(26, "ethernet-3Mbit");  
        typesMap.put(27, "nsip"); 
        typesMap.put(28, "slip"); 
        typesMap.put(29, "ultra"); 
        typesMap.put(30, "ds3"); 
        typesMap.put(31, "sip"); 
        typesMap.put(32, "frame-relay"); 
    }
    
    public static String getTypeLabel(int type) {
        return typesMap.get(type);
    }
}