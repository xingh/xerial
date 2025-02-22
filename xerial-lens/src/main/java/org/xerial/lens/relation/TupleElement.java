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
// TupleCell.java
// Since: 2009/05/13 9:20:19
//
// $URL$
// $Author$
//--------------------------------------
package org.xerial.lens.relation;

/**
 * A composite pattern interface for representing tree node and tuple structure.
 * Tuples containing this cell element allow non 1-NF (tuples can contain
 * tuples) data representation.
 * 
 * @author leo
 * 
 */
public interface TupleElement<NodeType extends TupleElement<NodeType>> {
    /**
     * Returns true if this element is a singleton, i.e. no more nested nodes
     * 
     * @return
     */
    public boolean isAtom();

    /**
     * Returns true if this cell is a tuple
     * 
     * @return
     */
    public boolean isTuple();

    /**
     * Get the number of elements contained in this element. When this element
     * is an atom, the size will be 1. When a tuple, the returned size is the
     * tuple size.
     * 
     * @return
     */
    public int size();

    /**
     * Get the tuple element at the specified index
     * 
     * @param index
     * @return
     */
    TupleElement<NodeType> get(TupleIndex index);

    /**
     * Get the tuple element at the specified index
     * 
     * @param index
     * @return
     */
    TupleElement<NodeType> get(int index);

    /**
     * If the cell at the specified index is a node, then return the node,
     * otherwise return null
     * 
     * @param index
     * @return node
     */
    NodeType getNode(TupleIndex index);

    /**
     * if this cell is node, then return node. otherwise return null
     * 
     * @return
     */
    NodeType castToNode();

    /**
     * If this cell is tuple, then return tuple. otherwise return null
     * 
     * @return
     */
    Tuple<NodeType> castToTuple();

    /**
     * Accept the visitor
     * 
     * @param visitor
     */
    void accept(TupleVisitor<NodeType> visitor);

}
