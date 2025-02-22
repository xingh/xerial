/*--------------------------------------------------------------------------
 *  Copyright 2008 Taro L. Saito
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
// OptionSetter.java
// Since: Oct 27, 2008 3:26:01 PM
//
// $URL$
// $Author$
//--------------------------------------
package org.xerial.util.opt.impl;

import org.xerial.core.XerialException;
import org.xerial.util.opt.OptionParserException;

/**
 * @author leo
 * 
 */
public interface OptionSetter {
    void setOption(Object bean, Object convertedValue) throws XerialException;

    Class< ? > getOptionDataType();

    boolean takesArgument();

    void initialize(Object bean) throws OptionParserException;

    String getParameterName();
}
