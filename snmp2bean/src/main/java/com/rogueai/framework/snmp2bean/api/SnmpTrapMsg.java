package com.rogueai.framework.snmp2bean.api;

import java.util.Map;

public interface SnmpTrapMsg {
    
    /**
     *  Message type.
     */
    String MSG_TYPE = "msg_type";
    /**
     *  Ipaddress of the node that sends the trap.
     */
    String NODE_ADDR = "node_addr";
    /**
     * Trap OID
     */
    String TRAP_OID = "trap_oid";
    
    /**
     * V1 Trap enterprise oid.
     */
    String ENTERPRISE = "enterprise";
    /**
     * V1 Trap agent_addr.
     */
    String AGENT_ADDR = "agent_addr";

    /** V1 Trap generic-trap     
            INTEGER {
            coldStart(0),
            warmStart(1),
            linkDown(2),
            linkUp(3),
            authenticationFailure(4),
            egpNeighborLoss(5),
            enterpriseSpecific(6)
            },
    */
    String GENERIC_TRAP = "generic_trap";
    /**
     * V1 Trap specific-trap
     */
    String SPECIFIC_TRAP = "specific_trap";
    
    /**
     * V1 Trap time_stamp
     */
    String TIME_STAMP = "time_stamp";

    String RECEIVED_TIME = "received_time";
    /**
     *  Get the ip address of the node that sends the trap.
     *  @return peer address
     */
    String getPeerAddr();

    /**
     * Get the trap oid. 
     * 
     * @return trap oid.
     */
    String getTrapOid();
    /**
     * Get the value map.
     * 
     * @return value map
     */
    Map<String, Object> toMap();

    
}
