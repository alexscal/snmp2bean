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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rogueai.framework.snmp2bean.api.SnmpSession;
import com.rogueai.framework.snmp2bean.api.SnmpSessionFactory;
import com.rogueai.framework.snmp2bean.api.SnmpTarget;
import com.rogueai.framework.snmp2bean.api.snmp4J.impl.Snmp4JErrorMsgProvider;
import com.rogueai.framework.snmp2bean.api.snmp4J.impl.Snmp4JSession;
import com.rogueai.framework.snmp2bean.api.snmp4J.impl.Snmp4JSmiTypeProvider;

public class Snmp4JSessionFactory implements SnmpSessionFactory {
    
    protected final Snmp4JSmiTypeProvider typeProvider;

    protected final Snmp4JErrorMsgProvider errorMsgProvider;

    private static final Logger LOGGER = LoggerFactory.getLogger(Snmp4JSessionFactory.class);
    
    public Snmp4JSessionFactory() {
        typeProvider = new Snmp4JSmiTypeProvider();
        errorMsgProvider = new Snmp4JErrorMsgProvider();
    }

    public SnmpSession createSnmpSession(SnmpTarget target) throws IOException {
        Snmp4JSession session = new Snmp4JSession(target);
        session.setSmiTypeProvider(typeProvider);
        session.setSnmpErrorMsgProvider(errorMsgProvider);
        return session;
    }
    
}
