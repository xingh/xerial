/*--------------------------------------------------------------------------
 *  Copyright 2007 Taro L. Saito
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
// ComplexType.java
// Since: Dec 19, 2007 6:26:23 PM
//
// $URL$
// $Author$
//--------------------------------------
package org.xerial.util.bean.sample;

import java.util.Map;

public class ComplexType
{
    ComplexMap table = new ComplexMap();

    public ComplexType()
    {}

    public Map getTable()
    {
        return table;
    }

    /*
    public void setTable(ComplexMap table)
    {
        this.table = table;
    }
    */

    public void putTable(Integer key, PersonTable value)
    {
        table.put(key, value);
    }
}
