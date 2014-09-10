/*******************************************************************************
 * Copyright 2014 Rogueai.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.rogueai.framework.snmp2bean.test.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.snmp4j.TransportMapping;
import org.snmp4j.TransportStateReference;
import org.snmp4j.smi.Address;
import org.snmp4j.transport.TransportListener;

public abstract class AbstractTransportMapping<A extends Address> implements TransportMapping<A> {
    
    protected List<TransportListener> transportListener = new ArrayList<TransportListener>(1);
    protected int maxInboundMessageSize = (1 << 16) - 1;
    protected boolean asyncMsgProcessingSupported = true;
    
    public abstract Class<? extends Address> getSupportedAddressClass();
    
    public abstract void sendMessage(A address, byte[] message,
            TransportStateReference tmStateReference)
                    throws IOException;
    
    public synchronized void addTransportListener(TransportListener l) {
        if (!transportListener.contains(l)) {
            List<TransportListener> tlCopy =
                    new ArrayList<TransportListener>(transportListener);
            tlCopy.add(l);
            transportListener = tlCopy;
        }
    }
    
    public synchronized void removeTransportListener(TransportListener l) {
        if (transportListener != null && transportListener.contains(l)) {
            List<TransportListener> tlCopy =
                    new ArrayList<TransportListener>(transportListener);
            tlCopy.remove(l);
            transportListener = tlCopy;
        }
    }
    
    protected void fireProcessMessage(Address address,  ByteBuffer buf,
            TransportStateReference tmStateReference) {
        if (transportListener != null) {
            for (TransportListener aTransportListener : transportListener) {
                aTransportListener.processMessage(this, address, buf, tmStateReference);
            }
        }
    }
    
    public abstract void close() throws IOException;
    public abstract void listen() throws IOException;
    
    public int getMaxInboundMessageSize() {
        return maxInboundMessageSize;
    }
    
    public boolean isAsyncMsgProcessingSupported() {
        return asyncMsgProcessingSupported;
    }
    
    public void setAsyncMsgProcessingSupported(
            boolean asyncMsgProcessingSupported) {
        this.asyncMsgProcessingSupported = asyncMsgProcessingSupported;
    }
    
}
