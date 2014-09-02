package com.rogueai.framework.snmp2bean.api;

import com.rogueai.framework.snmp2bean.enums.SmiType;

public interface SmiTypeProvider {
    
    Class getSmiType(SmiType smiTypeEnum);
    
}
