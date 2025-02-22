/*--------------------------------------------------------------------------
 *  Copyright 2009 Taro L. Saito
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *--------------------------------------------------------------------------*/
//--------------------------------------
// XerialJ
//
// ObjectHandler.java
// Since: 2009/07/04 10:58:39
//
// $URL$
// $Author$
//--------------------------------------
package org.xerial.util;

/**
 * Handler interface of a sequence of objects
 * 
 * @author leo
 * 
 */
public interface ObjectHandler<T> {

    /**
     * Called before starting the object retrieval
     * 
     * @throws Exception
     */
    public void init() throws Exception;

    /**
     * @param input
     * @throws Exception
     */
    public void handle(T input) throws Exception;

    /**
     * Called at the end of the object retrieval
     * 
     * @throws Exception
     */
    public void finish() throws Exception;
}
