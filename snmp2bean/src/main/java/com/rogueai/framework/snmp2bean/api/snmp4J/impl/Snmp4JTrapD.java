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
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;

import com.rogueai.framework.snmp2bean.api.SnmpTrapD;
import com.rogueai.framework.snmp2bean.api.SnmpTrapHandler;
import com.rogueai.framework.snmp2bean.api.SnmpTrapMsg;

public class Snmp4JTrapD implements SnmpTrapD, CommandResponder {
    
    private static Logger LOGGER = Logger.getLogger(Snmp4JTrapD.class);
    
    private String listenAddress = "udp:0.0.0.0/162";
    
    private int poolSize = 3;

    private MultiThreadedMessageDispatcher dispatcher;

    private Snmp snmp = null;

    private ThreadPool threadPool;
    
    private List<SnmpTrapHandler> handlerList = Collections.synchronizedList(new ArrayList<SnmpTrapHandler>(5));
    
    /**
     * Address Format: udp:0.0.0.0/162
     * 
     * @param address
     */
    public void setListenAddress(String address) {
        this.listenAddress = address;
    }
    
    public void setPoolSize(int size) {
        this.poolSize = size;
    }

    public void start() throws Exception {
        init();
        snmp.addCommandResponder(this);
    }

    private void init() throws UnknownHostException, IOException {
        //create thread pool
        threadPool = ThreadPool.create("Trap Receiver ThreadPool", poolSize);
        //create dispatcher
        dispatcher = new MultiThreadedMessageDispatcher(threadPool,
                new MessageDispatcherImpl());
        //create transport
        Address address = GenericAddress.parse(this.listenAddress);
        TransportMapping transport = null;
        if (address instanceof UdpAddress) {
            transport = new DefaultUdpTransportMapping((UdpAddress) address);
        } else {
            transport = new DefaultTcpTransportMapping((TcpAddress) address);
        }
        //create snmp
        snmp = new Snmp(dispatcher, transport);
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
        USM usm = new USM(
                SecurityProtocols.getInstance(),
                new OctetString(MPv3.createLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);
        snmp.listen();
    }

    public void stop() throws Exception {
        snmp.close();
        threadPool.cancel();
    }

    public void processPdu(CommandResponderEvent event) {
        try {
            PDU pdu = event.getPDU();
            if( !(pdu.getType() == PDU.TRAP || pdu.getType() == PDU.V1TRAP) ) {
                LOGGER.warn("Not a trap.");
                return;
            }
            LOGGER.info("A Trap Received! " + pdu);
            SnmpTrapMsg trapMsg = new Snmp4JTrapMsg(event.getPeerAddress(), pdu);
            LOGGER.info("TrapMessage Created:" + trapMsg.toMap().toString());
            SnmpTrapHandler[] handlers = getHandlers();
            for (SnmpTrapHandler handler : handlers) {
                if(handler.onTrapMsg(trapMsg)) {
                    event.setProcessed(true);
                    return;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error happened when handling trap!", e);
        }
    }

    public void addTrapHandler(SnmpTrapHandler handler) {
        handlerList.add(handler);
    }

    public void removeTrapHandler(SnmpTrapHandler handler) {
        handlerList.remove(handler);
    }
    
    SnmpTrapHandler[] getHandlers() {
        return handlerList.toArray(new SnmpTrapHandler[handlerList.size()]);
    }
}
