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
import java.io.Serializable;
import java.util.List;

import com.rogueai.framework.snmp2bean.exception.SnmpAnnotationException;
import com.rogueai.framework.snmp2bean.exception.SnmpException;

public interface SnmpSession {
 
    int getRetries();

    void setRetries(int retries);

    int getTimeout();

    void setTimeout(int timeout);

    <T> T get(Class<T> scalarClass) throws IOException, SnmpException, SnmpAnnotationException;

    <T> T get(Class<T> scalarClass, String[] fields) throws IOException, SnmpException, SnmpAnnotationException;
    
    <T> List<T> getTable(Class<T> entryClass) throws IOException, SnmpException, SnmpAnnotationException;

    <T> T getByIndex(Class<T> entryClass, Serializable indices) throws IOException, SnmpException, SnmpAnnotationException;

    <T> T getByIndex(Class<T> entryClass, Serializable indices, String[] fields) throws IOException, SnmpException, SnmpAnnotationException;

    void set(Object object) throws IOException, SnmpException,  SnmpAnnotationException;

    void create(Object entry) throws IOException, SnmpException,  SnmpAnnotationException;

    void delete(Object entry) throws IOException, SnmpException,  SnmpAnnotationException;

    void close() throws IOException;
    
}
