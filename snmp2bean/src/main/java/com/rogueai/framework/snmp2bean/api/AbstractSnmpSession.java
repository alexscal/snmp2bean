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

public abstract class AbstractSnmpSession implements SnmpSession {
    
    private int retries = 3;

    private int timeout = 3000;

    private SmiTypeProvider smiTypeProvider;

    private SnmpErrorMsgProvider errorMsgProvider;

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setSmiTypeProvider(SmiTypeProvider typeProvider) {
        this.smiTypeProvider = typeProvider;
    }

    public void setSnmpErrorMsgProvider(SnmpErrorMsgProvider errorMsgProvider) {
        this.errorMsgProvider = errorMsgProvider;
    }

    protected final SmiTypeProvider getSmiTypeProvider() {
        return smiTypeProvider;
    }

    protected final SnmpErrorMsgProvider getSnmpErrorMsgProvider() {
        return errorMsgProvider;
    }
}