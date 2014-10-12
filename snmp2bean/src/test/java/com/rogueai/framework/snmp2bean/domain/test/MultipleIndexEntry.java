package com.rogueai.framework.snmp2bean.domain.test;

import com.rogueai.framework.snmp2bean.annotation.MibIndex;
import com.rogueai.framework.snmp2bean.annotation.MibObjectType;
import com.rogueai.framework.snmp2bean.annotation.MibObjectType.Access;
import com.rogueai.framework.snmp2bean.annotation.MibTable;
import com.rogueai.framework.snmp2bean.enums.SmiType;

@MibTable
public class MultipleIndexEntry {
   
        @MibIndex(no = 0, length = 1)
        @MibObjectType(oid = ".1.3.6.1.4.1.3711.24.1.1.7.2.4.1.1", smiType = SmiType.UNSIGNED32, access = Access.READ)
        private Long pduOutThreshPduNum; //  The number of the PDU on which the outlet resides.

        @MibIndex(no = 1, length = 1)
        @MibObjectType(oid = ".1.3.6.1.4.1.3711.24.1.1.7.2.4.1.2", smiType = SmiType.UNSIGNED32, access = Access.READ)
        private  Long pduOutThreshOutNum; // The outlet number.
        
        @MibObjectType(oid = ".1.3.6.1.4.1.3711.24.1.1.7.2.4.1.3", smiType = SmiType.INTEGER, access = Access.READ)
        private Integer pduOutThreshRS; // The table row status.
        
        @MibObjectType(oid = ".1.3.6.1.4.1.3711.24.1.1.7.2.4.1.4", smiType = SmiType.UNSIGNED32, access = Access.READ)
        private Long pduOutRMSAmpsUTL; // Upper threshold of pduOutRMSAmpsValue in 0.1 Amps
        
        @MibObjectType(oid = ".1.3.6.1.4.1.3711.24.1.1.7.2.4.1.5", smiType = SmiType.UNSIGNED32, access = Access.READ)
        private Long pduOutRMSAmpsLTL; // Lower threshold of pduOutRMSAmpsValue in 0.1 Amps
       
        @MibObjectType(oid = ".1.3.6.1.4.1.3711.24.1.1.7.2.4.1.6", smiType = SmiType.UNSIGNED32, access = Access.READ)
        private Long pduOutRMSAmpsDeltaPos; // Rising rate of change threshold of pduOutRMSAmpsValue in 0.1 Amps per minute
        
        @MibObjectType(oid = ".1.3.6.1.4.1.3711.24.1.1.7.2.4.1.7", smiType = SmiType.UNSIGNED32, access = Access.READ)
        private Long pduOutRMSAmpsDeltaNeg; // Falling rate of change threshold of pduOutRMSAmpsValue in 0.1 Amps per minute

        @MibObjectType(oid = ".1.3.6.1.4.1.3711.24.1.1.7.2.4.1.8", smiType = SmiType.UNSIGNED32, access = Access.READ)
        private Long pduOutMeanKVAUTL; // Upper threshold of pduOutMeanKVAValue in 0.1 Amps
        
        @MibObjectType(oid = ".1.3.6.1.4.1.3711.24.1.1.7.2.4.1.9", smiType = SmiType.UNSIGNED32, access = Access.READ)
        private Long pduOutMeanKVALTL; //  Lower threshold of pduOutMeanKVAValue in 0.1 Amps
        
        @MibObjectType(oid = ".1.3.6.1.4.1.3711.24.1.1.7.2.4.1.10", smiType = SmiType.UNSIGNED32, access = Access.READ)
        private Long pduOutMeanKVADeltaPos; // Rising rate of change threshold of pduOutMeanKVAValue in 0.1 Amps per minute
        
        @MibObjectType(oid = ".1.3.6.1.4.1.3711.24.1.1.7.2.4.1.11", smiType = SmiType.UNSIGNED32, access = Access.READ)
        private Long pduOutMeanKVADeltaNeg; // Falling rate of change threshold of pduOutMeanKVAValue in 0.1 Amps per minute
        
        @MibObjectType(oid = ".1.3.6.1.4.1.3711.24.1.1.7.2.4.1.12", smiType = SmiType.UNSIGNED32, access = Access.READ)
        private Long pduOutPFactorUTL; // Upper threshold of pduOutPFactorValue in 0.1 Amps
        
        @MibObjectType(oid = ".1.3.6.1.4.1.3711.24.1.1.7.2.4.1.13", smiType = SmiType.UNSIGNED32, access = Access.READ)
        private Long pduOutPFactorLTL; // Lower threshold of pduOutPFactorValue in 0.1 Amps

        public Long getPduOutThreshPduNum() {
            return pduOutThreshPduNum;
        }

        public void setPduOutThreshPduNum(Long pduOutThreshPduNum) {
            this.pduOutThreshPduNum = pduOutThreshPduNum;
        }

        public Long getPduOutThreshOutNum() {
            return pduOutThreshOutNum;
        }

        public void setPduOutThreshOutNum(Long pduOutThreshOutNum) {
            this.pduOutThreshOutNum = pduOutThreshOutNum;
        }

        public Integer getPduOutThreshRS() {
            return pduOutThreshRS;
        }

        public void setPduOutThreshRS(Integer pduOutThreshRS) {
            this.pduOutThreshRS = pduOutThreshRS;
        }

        public Long getPduOutRMSAmpsUTL() {
            return pduOutRMSAmpsUTL;
        }

        public void setPduOutRMSAmpsUTL(Long pduOutRMSAmpsUTL) {
            this.pduOutRMSAmpsUTL = pduOutRMSAmpsUTL;
        }

        public Long getPduOutRMSAmpsLTL() {
            return pduOutRMSAmpsLTL;
        }

        public void setPduOutRMSAmpsLTL(Long pduOutRMSAmpsLTL) {
            this.pduOutRMSAmpsLTL = pduOutRMSAmpsLTL;
        }

        public Long getPduOutRMSAmpsDeltaPos() {
            return pduOutRMSAmpsDeltaPos;
        }

        public void setPduOutRMSAmpsDeltaPos(Long pduOutRMSAmpsDeltaPos) {
            this.pduOutRMSAmpsDeltaPos = pduOutRMSAmpsDeltaPos;
        }

        public Long getPduOutRMSAmpsDeltaNeg() {
            return pduOutRMSAmpsDeltaNeg;
        }

        public void setPduOutRMSAmpsDeltaNeg(Long pduOutRMSAmpsDeltaNeg) {
            this.pduOutRMSAmpsDeltaNeg = pduOutRMSAmpsDeltaNeg;
        }

        public Long getPduOutMeanKVAUTL() {
            return pduOutMeanKVAUTL;
        }

        public void setPduOutMeanKVAUTL(Long pduOutMeanKVAUTL) {
            this.pduOutMeanKVAUTL = pduOutMeanKVAUTL;
        }

        public Long getPduOutMeanKVALTL() {
            return pduOutMeanKVALTL;
        }

        public void setPduOutMeanKVALTL(Long pduOutMeanKVALTL) {
            this.pduOutMeanKVALTL = pduOutMeanKVALTL;
        }

        public Long getPduOutMeanKVADeltaPos() {
            return pduOutMeanKVADeltaPos;
        }

        public void setPduOutMeanKVADeltaPos(Long pduOutMeanKVADeltaPos) {
            this.pduOutMeanKVADeltaPos = pduOutMeanKVADeltaPos;
        }

        public Long getPduOutMeanKVADeltaNeg() {
            return pduOutMeanKVADeltaNeg;
        }

        public void setPduOutMeanKVADeltaNeg(Long pduOutMeanKVADeltaNeg) {
            this.pduOutMeanKVADeltaNeg = pduOutMeanKVADeltaNeg;
        }

        public Long getPduOutPFactorUTL() {
            return pduOutPFactorUTL;
        }

        public void setPduOutPFactorUTL(Long pduOutPFactorUTL) {
            this.pduOutPFactorUTL = pduOutPFactorUTL;
        }

        public Long getPduOutPFactorLTL() {
            return pduOutPFactorLTL;
        }

        public void setPduOutPFactorLTL(Long pduOutPFactorLTL) {
            this.pduOutPFactorLTL = pduOutPFactorLTL;
        }
    
}
