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

import java.io.IOException;

import org.snmp4j.Target;

import com.rogueai.framework.snmp2bean.api.snmp4J.impl.Snmp4JWrapper;

public interface SnmpSession {
 
    int getRetries();

    void setRetries(int retries);

    int getTimeout();

    void setTimeout(int timeout);
    
    Snmp4JWrapper getSnmpWrapper();
    
    Target getReadTarget();
    
    Target getWriteTarget();

    void close() throws IOException;
    
    SmiTypeProvider getSmiTypeProvider();
    
    SnmpErrorMsgProvider getSnmpErrorMsgProvider();
    
}
