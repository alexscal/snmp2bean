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
package com.rogueai.framework.snmp2bean.api;

public interface SnmpTarget {
    
    int V1 = 0;

    int V2C = 1;

    int V3 = 3;

    String getCommunity();

    void setCommunity(String community);

    String getIp();

    void setIp(String ip);

    int getPort();

    void setPort(int port);

    int getVersion();

    void setVersion(int version);

    String getWriteCommunity();

    void setWriteCommunity(String writeCommunity);
    
}
