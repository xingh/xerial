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
// SilkEvent.java
// Since: Jan 30, 2009 3:30:38 PM
//
// $URL$
// $Author$
//--------------------------------------
package org.xerial.silk;

/**
 * Pull parsing events of Silk data format
 * 
 * @author leo
 * 
 */
public enum SilkEventType {
    UNKNOWN, NODE, BLOCK_NODE, FUNCTION, COMMENT_LINE, BLANK_LINE, DATA_LINE, END_OF_FILE, PREAMBLE, MULTILINE_SEPARATOR;
}
