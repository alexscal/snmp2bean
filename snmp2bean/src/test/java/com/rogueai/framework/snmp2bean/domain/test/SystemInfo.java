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
package com.rogueai.framework.snmp2bean.domain.test;

import com.rogueai.framework.snmp2bean.annotation.MibObjectType;
import com.rogueai.framework.snmp2bean.annotation.MibObjectType.Access;
import com.rogueai.framework.snmp2bean.enums.SmiType;

public class SystemInfo {
 
    @MibObjectType(oid = "1.3.6.1.2.1.1.1.0", smiType = SmiType.DISPLAY_STRING, access = Access.READ)
    private String sysDesc;

    @MibObjectType(oid = "1.3.6.1.2.1.1.2.0", smiType = SmiType.OID, access = Access.READ)
    private String sysObjectID;

    @MibObjectType(oid = "1.3.6.1.2.1.1.3.0", smiType = SmiType.TIMETICKS, access = Access.READ)
    private long sysUpTime;

    @MibObjectType(oid = "1.3.6.1.2.1.1.4.0", smiType = SmiType.DISPLAY_STRING, access = Access.WRITE)
    private String sysContact;

    @MibObjectType(oid = "1.3.6.1.2.1.1.5.0", smiType = SmiType.DISPLAY_STRING, access = Access.WRITE)
    private String sysName;

    @MibObjectType(oid = "1.3.6.1.2.1.1.6.0", smiType = SmiType.DISPLAY_STRING, access = Access.WRITE)
    private String sysLocation;

    public String getSysContact() {
        return sysContact;
    }

    public void setSysContact(String sysContact) {
        this.sysContact = sysContact;
    }

    public String getSysDesc() {
        return sysDesc;
    }

    public void setSysDesc(String sysDesc) {
        this.sysDesc = sysDesc;
    }

    public String getSysLocation() {
        return sysLocation;
    }

    public void setSysLocation(String sysLocation) {
        this.sysLocation = sysLocation;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysObjectID() {
        return sysObjectID;
    }

    public void setSysObjectID(String sysObjectID) {
        this.sysObjectID = sysObjectID;
    }

    public long getSysUpTime() {
        return sysUpTime;
    }

    public void setSysUpTime(long sysUpTime) {
        this.sysUpTime = sysUpTime;
    }
}