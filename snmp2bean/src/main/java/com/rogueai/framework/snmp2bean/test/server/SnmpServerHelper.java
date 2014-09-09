package com.rogueai.framework.snmp2bean.test.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.MutablePDU;
import org.snmp4j.PDU;
import org.snmp4j.asn1.BER;
import org.snmp4j.asn1.BER.MutableByte;
import org.snmp4j.asn1.BERInputStream;
import org.snmp4j.asn1.BEROutputStream;
import org.snmp4j.mp.MessageProcessingModel;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OctetString;

import com.rogueai.framework.snmp2bean.api.SnmpTarget;

public class SnmpServerHelper {
    
    public static final int ID = MessageProcessingModel.MPv2c;
    private static final Logger LOGGER = LoggerFactory.getLogger(SnmpServerHelper.class);
    
    public PDU getPDUFromRequest(byte[] bytes) throws IOException {
        OctetString octetString = new OctetString(bytes);
        
        ByteBuffer buff = ByteBuffer.wrap(octetString.getValue());
        
        MutableByte mutableByte = new MutableByte();
        BERInputStream wholeMsg = new BERInputStream(buff);
        int length = BER.decodeHeader(wholeMsg, mutableByte);
        int startPos = (int)wholeMsg.getPosition();
        
        if (mutableByte.getValue() != BER.SEQUENCE) {
            String txt = "SNMPv2c PDU must start with a SEQUENCE";
            throw new IOException(txt);
        }
        Integer32 version = new Integer32();
        version.decodeBER(wholeMsg);
        
        OctetString securityName = new OctetString();
        securityName.decodeBER(wholeMsg);
        
        MutablePDU pdu = new MutablePDU();
        CommunityTarget target = new CommunityTarget();
        target.setVersion(SnmpConstants.version2c);
        
        PDU v2cPDU = new PDU();
        
        pdu.setPdu(v2cPDU);
        v2cPDU.decodeBER(wholeMsg);
        
        BER.checkSequenceLength(length, (int)wholeMsg.getPosition() - startPos, v2cPDU);
        
        return v2cPDU;
        
    }
    
    public byte[] getBytesMessageFromPDU(PDU pdu, SnmpTarget target) throws IOException {
        
        if (target == null) {
            throw new IOException();
        }
        
        OctetString community = new OctetString(target.getCommunity());
        Integer32 version = new Integer32(target.getVersion());
        
        // compute total length
        int length = pdu.getBERLength();
        length += community.getBERLength();
        length += version.getBERLength();
        
        ByteBuffer buf = ByteBuffer.allocate(length + BER.getBERLengthOfLength(length) + 1);
        BEROutputStream outgoingMessage = new BEROutputStream();
        // set the buffer of the outgoing message
        outgoingMessage.setBuffer(buf);
        
        // encode the message
        BER.encodeHeader(outgoingMessage, BER.SEQUENCE, length);
        version.encodeBER(outgoingMessage);
        
        community.encodeBER(outgoingMessage);
        pdu.encodeBER(outgoingMessage);
        
        byte[] messageBytes = outgoingMessage.getBuffer().array();
        
        return messageBytes;
    }
    
    
}