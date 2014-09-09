package com.rogueai.framework.snmp2bean.test.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;
import org.snmp4j.SNMP4JSettings;
import org.snmp4j.TransportStateReference;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.WorkerTask;

import com.rogueai.framework.snmp2bean.api.SnmpTarget;
import com.rogueai.framework.snmp2bean.api.snmp4J.impl.Snmp4JTarget;

public class DummyTransport<A extends IpAddress> extends AbstractTransportMapping<A> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DummyTransport.class);
    
    private final Queue<OctetString> requests = new ConcurrentLinkedQueue<OctetString>();
    private final Queue<OctetString> responses = new ConcurrentLinkedQueue<OctetString>();
    private boolean listening;
    private A listenAddress;
    private A receiverAddress;
    private WorkerTask listenThread;
    private long sessionID = 0;
    
    private SnmpOIDMap snmpOIDMap;
    
    public DummyTransport() {
        listening = false;
    }
    
    public DummyTransport(A senderAddress) {
        this.listenAddress = senderAddress;
    }
    
    public DummyTransport(A senderAddress, A receiverAddress) {
        this.listenAddress = senderAddress;
        this.receiverAddress = receiverAddress;
    }
    
    @Override
    public Class<? extends Address> getSupportedAddressClass() {
        return IpAddress.class;
    }
    
    @Override
    public A getListenAddress() {
        return listenAddress;
    }
    
    public void setListenAddress(A listenAddress) {
        this.listenAddress = listenAddress;
    }
    
    @Override
    public void sendMessage(A address, byte[] message, TransportStateReference tmStateReference) throws IOException {
        synchronized (requests) {
            OctetString e = new OctetString(message);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Send request message to '"+address+"': "+ e.toHexString());
            }
            requests.add(e);
        }
    }
    
    public byte[] processRequestAndGenerateResponse(byte[] bytes) {
        synchronized (requests) {
            SnmpServerHelper snmpServerHelper = new SnmpServerHelper();
            try {
                PDU pduFromRequest = snmpServerHelper.getPDUFromRequest(bytes);
                pduFromRequest.setType(PDU.RESPONSE);
                
                if (pduFromRequest.size() > 0) {
                    
                    Vector<? extends VariableBinding> variableBindings = pduFromRequest.getVariableBindings();
                    for (VariableBinding variableBinding : variableBindings) {
                        String oid = variableBinding.getOid().toDottedString();
                        String prop = snmpOIDMap.getPropertyValueByKey(oid);
                        
                        if (prop != null) {
                            Variable v = new OctetString(prop);
                            variableBinding.setVariable(v);
                            
                            if (LOGGER.isDebugEnabled()) 
                                LOGGER.debug("OID: " + oid + " : " + v.toString());
                        }
                    }
                }
                
                SnmpTarget target = new Snmp4JTarget("");
                
                target.setCommunity("public");
                target.setVersion(SnmpConstants.version2c);
                byte[] bytesMessageFromPDU = snmpServerHelper.getBytesMessageFromPDU(pduFromRequest, target);
                
                return bytesMessageFromPDU;
                
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            } 
            
            return null;
        }
    }
    
    
    @Override
    public void close() throws IOException {
        listening = false;
        listenThread.terminate();
        try {
            listenThread.join();
        } catch (InterruptedException e) {
            // ignore
        }
        responses.clear();
    }
    
    @Override
    public void listen() throws IOException {
        /**/
        listening = true;
        sessionID++;
        QueueProcessor listener = new QueueProcessor(requests, this);
        listenThread = SNMP4JSettings.getThreadFactory().createWorkerThread("DummyTransportMapping_"+getListenAddress(), listener, true);
        listenThread.run();
    }
    
    @Override
    public boolean isListening() {
        return listening;
    }
    
    private class QueueProcessor implements WorkerTask {
        
        private volatile boolean stop;
        private Queue<OctetString> queue;
        private AbstractTransportMapping tm;
        
        public QueueProcessor(Queue<OctetString> queue, AbstractTransportMapping tm) {
            this.queue = queue;
            this.tm = tm;
        }
        
        @Override
        public void run() {
            while (!stop) {
                OctetString nextMessage = null;
                nextMessage = queue.poll();
                if (nextMessage != null) {
                    LOGGER.info("Got a message, let's process the response");
                    TransportStateReference stateReference = new TransportStateReference(DummyTransport.this, listenAddress, null, SecurityLevel.undefined, SecurityLevel.undefined, false, sessionID);
                    byte[] response = processRequestAndGenerateResponse(nextMessage.toByteArray());
                    ByteBuffer bis = ByteBuffer.wrap(response);
                    tm.fireProcessMessage(receiverAddress, bis, stateReference);
                }
                else {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        LOGGER.warn("Interrupted QueueProcessor: "+e.getMessage());
                    }
                }
            }
        }
        
        @Override
        public void terminate() {
            stop = true;
        }
        
        @Override
        public void join() throws InterruptedException {
            stop = true;
            synchronized (this) {
                // wait until run is stopped
            }
        }
        
        @Override
        public void interrupt() {
            stop = true;
        }
    }
    
    public void setSnmpOIDMap(SnmpOIDMap snmpOIDMap) {
        this.snmpOIDMap = snmpOIDMap;
    }
}