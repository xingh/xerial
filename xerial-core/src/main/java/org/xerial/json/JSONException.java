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
// XerialJ Project
//
// JSONException.java
// Since: 2007/04/26
//
// $URL$ 
// $Author$
//--------------------------------------
package org.xerial.json;

import org.xerial.core.XerialException;

/**
 * Exception class related JSON
 * 
 * @author leo
 * 
 */
public class JSONException extends XerialException
{
    private static final long serialVersionUID = 1L;

    /**
     * @param errorCode
     */
    public JSONException(JSONErrorCode errorCode) {
        super(errorCode);
    }

    public JSONException(JSONErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * @param errorCode
     * @param message
     * @param e
     */
    public JSONException(JSONErrorCode errorCode, String message, Throwable e) {
        super(errorCode, message, e);
    }

    /**
     * @param errorCode
     * @param e
     */
    public JSONException(JSONErrorCode errorCode, Throwable e) {
        super(errorCode);
    }

    /**
     * Gets the error code
     * 
     * @return the error code
     */
    @SuppressWarnings("unchecked")
    public JSONErrorCode getErrorCode() {
        return JSONErrorCode.class.cast(errorCode);
    }
}
