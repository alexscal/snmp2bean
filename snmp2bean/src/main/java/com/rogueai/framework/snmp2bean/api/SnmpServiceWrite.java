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

import com.rogueai.framework.snmp2bean.exception.SnmpAnnotationException;
import com.rogueai.framework.snmp2bean.exception.SnmpException;

public interface SnmpServiceWrite {
    
    void set(Object object) throws IOException, SnmpException,  SnmpAnnotationException;

    void create(Object entry) throws IOException, SnmpException,  SnmpAnnotationException;

    void delete(Object entry) throws IOException, SnmpException,  SnmpAnnotationException;
    
}
