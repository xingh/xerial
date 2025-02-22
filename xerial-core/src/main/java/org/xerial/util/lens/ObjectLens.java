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
// ObjectLens.java
// Since: 2009/05/12 19:52:38
//
// $URL$
// $Author$
//--------------------------------------
package org.xerial.util.lens;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xerial.core.XerialException;
import org.xerial.util.Pair;
import org.xerial.util.ReflectionUtil;
import org.xerial.util.StringUtil;
import org.xerial.util.TypeInfo;
import org.xerial.util.bean.BeanUtil;
import org.xerial.util.lens.impl.ParameterGetter;
import org.xerial.util.lens.impl.ParameterSetter;
import org.xerial.util.lens.impl.RelationSetter;
import org.xerial.util.log.Logger;

/**
 * Lens is information of object interface, e.g., methods and fields that can be
 * modified dynamically. This {@link ObjectLens#getObjectLens(Class)} returns a
 * holder (called ObjectLens) of public fields (except array types) and public
 * setter/getter/putter methods defined in a given class.
 * 
 * 
 * @author leo
 * 
 */
public class ObjectLens {

    private static Logger _logger = Logger.getLogger(ObjectLens.class);

    private static HashMap<Class< ? >, ObjectLens> cache = new HashMap<Class< ? >, ObjectLens>();

    /**
     * Get the lens of the target type
     * 
     * @param target
     * @return lens of the target type
     */
    public static ObjectLens getObjectLens(Class< ? > target) {
        if (cache.containsKey(target))
            return cache.get(target);
        else {
            cache.put(target, new ObjectLens(target));
            return getObjectLens(target);
        }
    }

    private final Class< ? > targetType;

    private final List<ParameterGetter> getterContainer = new ArrayList<ParameterGetter>();
    private final List<ParameterSetter> setterContainer = new ArrayList<ParameterSetter>();

    private final HashMap<String, ParameterGetter> getterIndex = new HashMap<String, ParameterGetter>();
    private final HashMap<String, ParameterSetter> setterIndex = new HashMap<String, ParameterSetter>();

    public static String getCanonicalParameterName(String paramName) {
        return StringUtil.varNameToCanonicalName(paramName);
    }

    private final List<RelationSetter> relationSetterContainer = new ArrayList<RelationSetter>();
    private ParameterSetter valueSetter = null;

    private RelationSetter propertySetter = null;
    private ParameterGetter propertyGetter = null;

    public Object getParameter(Object target, String parameterName) throws XerialException {
        ParameterGetter getter = getterIndex.get(getCanonicalParameterName(parameterName));
        if (getter == null)
            return null;

        return getter.get(target);
    }

    public Object getParameter(Object target, String parameterName, Object key)
            throws XerialException {
        ParameterGetter getter = getterIndex.get(getCanonicalParameterName(parameterName));
        if (getter == null)
            return getProperty(target, key != null ? key.toString() : null);

        if (getter.returnsMapType())
            return getter.get(target, key.toString());
        else
            return getter.get(target);
    }

    public void setParameter(Object target, String parameterName, Object key, Object value)
            throws XerialException {

        ParameterSetter setter = setterIndex.get(getCanonicalParameterName(parameterName));
        if (setter == null) {
            setProperty(target, key, value);
        }
        else {
            if (setter.acceptKeyAndValue())
                setter.bind(target, key, value);
            else
                setter.bind(target, value);
        }
    }

    public void setParameter(Object target, String parameterName, Object value)
            throws XerialException {

        ParameterSetter setter = setterIndex.get(getCanonicalParameterName(parameterName));
        if (setter == null)
            return;

        setter.bind(target, value);
    }

    public boolean hasPropertySetter() {
        return propertySetter != null;
    }

    RelationSetter getPropertySetter() {
        return propertySetter;
    }

    public Object getProperty(Object target, String key) throws XerialException {
        if (propertyGetter == null)
            return null;

        return propertyGetter.get(target, key);
    }

    /**
     * Invoke property setter put(key, value) of the target object
     * 
     * @param target
     * @param key
     * @param value
     * @throws XerialException
     */
    public void setProperty(Object target, Object key, Object value) throws XerialException {
        if (propertySetter == null)
            return;

        propertySetter.bind(target, key, value);
    }

    public List<ParameterSetter> getSetterList() {
        return Collections.unmodifiableList(setterContainer);
    }

    public List<RelationSetter> getRelationSetterList() {
        return Collections.unmodifiableList(relationSetterContainer);
    }

    public List<ParameterGetter> getGetterContainer() {
        return Collections.unmodifiableList(getterContainer);
    }

    public ParameterSetter getValueSetter() {
        return valueSetter;
    }

    public boolean hasAttributes() {
        return !getterContainer.isEmpty();
    }

    public ObjectLens(Class< ? > targetType) {
        this.targetType = targetType;
        prepareBindRules(targetType);
    }

    public Class< ? > getTargetType() {
        return targetType;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", setterContainer, relationSetterContainer);
    }

    /**
     * Scans the field and getter/setter definitions in the given class, then
     * generates the mapping rules in the form of {@link ParameterSetter},
     * {@link ParameterGetter} and {@link RelationSetter}.
     * 
     * @param targetType
     */
    private void prepareBindRules(Class< ? > targetType) {
        // search for object parameters including superclass's ones 

        // scan public fields
        for (Field eachField : targetType.getFields()) {
            // ignore fields defined in the Object class
            Class< ? > parentClassOfTheField = eachField.getDeclaringClass();
            if (parentClassOfTheField == Object.class)
                continue;

            // looking for only public fields
            int fieldModifier = eachField.getModifiers();
            if (Modifier.isPublic(fieldModifier) && !Modifier.isTransient(fieldModifier)
                    && !Modifier.isStatic(fieldModifier)) {

                Class< ? > fieldType = eachField.getType();
                String paramName = eachField.getName();

                if (TypeInfo.isArray(fieldType)) {
                    // ignore the array field except the byte[] type
                    Class< ? > arrayElementType = TypeInfo.getArrayElementType(fieldType);
                    if (arrayElementType != null && byte.class == arrayElementType) {
                        // byte[] getter & setter
                        getterContainer.add(ParameterGetter.newFieldGetter(eachField, paramName));
                        setterContainer.add(ParameterSetter.newSetter(fieldType, paramName,
                                eachField));
                    }
                    continue;
                }
                else if (TypeInfo.isMap(fieldType)) {
                    Pair<String, String> keyValueName = pickRelationName(eachField.getName());
                    if (keyValueName == null) {
                        // infer key, value names from the class type
                        Pair<Class< ? >, Class< ? >> mapElementType = ReflectionUtil
                                .getGenericMapElementClasses(eachField);

                        Class< ? > keyType = mapElementType.getFirst();
                        Class< ? > valueType = mapElementType.getSecond();

                        keyValueName = new Pair<String, String>(keyType.getSimpleName(),
                                valueType.getSimpleName());

                        if (isBasicTypeOrObject(keyType) && isBasicTypeOrObject(valueType)) {

                            // named map class. e.g. Property tag;
                            setterContainer.add(ParameterSetter.newSetter(fieldType, paramName,
                                    eachField));
                            getterContainer.add(ParameterGetter.newPropertyFieldGetter(eachField,
                                    paramName));
                        }
                    }
                    else if (keyValueName.getFirst().equals("")
                            && keyValueName.getSecond().equals("")) {
                        // property (key, value) setter

                        propertySetter = RelationSetter.newMapSetter("key", "value", eachField);
                        getterContainer.add(ParameterGetter.newFieldGetter(eachField, paramName));
                        propertyGetter = ParameterGetter.newPropertyFieldGetter(eachField,
                                paramName);
                        continue;
                    }

                    relationSetterContainer.add(RelationSetter.newMapSetter(
                            keyValueName.getFirst(), keyValueName.getSecond(), eachField));
                }
                else if (TypeInfo.isCollection(fieldType)) {
                    Class< ? > elementType = ReflectionUtil.getRawClass(ReflectionUtil
                            .getGenericCollectionElementType(eachField));
                    setterContainer.add(ParameterSetter
                            .newSetter(elementType, paramName, eachField));
                    getterContainer.add(ParameterGetter.newFieldGetter(eachField, paramName));
                }
                else {
                    if (!paramName.equals("value"))
                        setterContainer.add(ParameterSetter.newSetter(fieldType, paramName,
                                eachField));
                    else
                        valueSetter = ParameterSetter.newSetter(fieldType, paramName, eachField);
                    getterContainer.add(ParameterGetter.newFieldGetter(eachField, paramName));
                }

            }

        }

        // -- scan methods
        // Parameter names used in setter/putter/adder/getter in the class definition 
        // overrides getter/setter defined for field parameters 
        for (Method eachMethod : targetType.getMethods()) {
            String methodName = eachMethod.getName();

            if (methodName.startsWith("add") || methodName.startsWith("set")) {
                Class< ? >[] argTypes = eachMethod.getParameterTypes();
                switch (argTypes.length) {
                    case 1: {
                        String paramName = pickPropertyName(methodName);
                        Class< ? > parentOfTheSetter = eachMethod.getDeclaringClass();
                        if ((TypeInfo.isCollection(parentOfTheSetter) || TypeInfo
                                .isMap(parentOfTheSetter)) && paramName.equals("all"))
                            break;

                        if (paramName.length() <= 0 && TypeInfo.isCollection(parentOfTheSetter)) {
                            Class< ? > elementType = BeanUtil.resolveActualTypeOfCollectionElement(
                                    targetType, argTypes[0]);
                            setterContainer.add(ParameterSetter.newSetter(elementType, "entry",
                                    eachMethod));
                        }
                        else
                            addNewSetter(setterContainer, paramName, eachMethod);
                        break;
                    }
                    case 2: {
                        if (TypeInfo.isCollection(eachMethod.getDeclaringClass())) {
                            break;
                        }

                        // relation adder

                        Pair<String, String> relName = pickRelationName(pickPropertyName(
                                methodName, false));
                        if (relName == null) {
                            // infer relation node names
                            if (TypeInfo.isMap(eachMethod.getDeclaringClass())) {

                                Class< ? >[] mapElementType = BeanUtil
                                        .resolveActualTypeOfMapElement(targetType,
                                                eachMethod.getParameterTypes());

                                // map.put(Key, Value)
                                setterContainer.add(ParameterSetter.newMapEntrySetter(
                                        mapElementType[0], mapElementType[1]));

                                // (entry, key)
                                setterContainer
                                        .add(ParameterSetter.newKeySetter(mapElementType[0]));
                                // (entry, value)
                                setterContainer.add(ParameterSetter
                                        .newValueSetter(mapElementType[1]));
                                continue;
                            }
                            else {
                                relName = new Pair<String, String>(
                                        getCanonicalParameterName(argTypes[0].getSimpleName()),
                                        getCanonicalParameterName(argTypes[1].getSimpleName()));
                            }
                        }

                        relationSetterContainer.add(RelationSetter.newRelationSetter(
                                relName.getFirst(), relName.getSecond(), eachMethod));
                        break;
                    }
                }

                continue;

            }
            else if (methodName.startsWith("put")) {
                Class< ? >[] argTypes = eachMethod.getParameterTypes();
                if (argTypes.length != 2)
                    continue;

                if (TypeInfo.isCollection(eachMethod.getDeclaringClass())) {
                    continue;
                }

                // relation adder
                Pair<String, String> relName = pickRelationName(pickPropertyName(methodName, false));
                if (relName == null) {
                    // infer relation node names
                    if (TypeInfo.isMap(eachMethod.getDeclaringClass())) {

                        Class< ? >[] mapElementType = BeanUtil.resolveActualTypeOfMapElement(
                                targetType, eachMethod.getParameterTypes());

                        if (isBasicTypeOrObject(mapElementType[0])
                                && isBasicTypeOrObject(mapElementType[1])) {
                            propertySetter = RelationSetter.newRelationSetter("key", "value",
                                    eachMethod);
                        }

                        // map.put(Key, Value)
                        setterContainer.add(ParameterSetter.newMapEntrySetter(mapElementType[0],
                                mapElementType[1]));

                        // (entry, key)
                        setterContainer.add(ParameterSetter.newKeySetter(mapElementType[0]));
                        // (entry, value)
                        setterContainer.add(ParameterSetter.newValueSetter(mapElementType[1]));
                        continue;
                    }
                    else {
                        propertySetter = RelationSetter.newRelationSetter("key", "value",
                                eachMethod);
                        continue;
                    }
                }

                relationSetterContainer.add(RelationSetter.newRelationSetter(relName.getFirst(),
                        relName.getSecond(), eachMethod));

                continue;
            }
            else if (methodName.startsWith("append")) {
                String paramName = pickPropertyName(methodName);
                addNewSetter(setterContainer, paramName, eachMethod);
                continue;
            }
            else if (methodName.startsWith("get")) {
                int argLength = eachMethod.getParameterTypes().length;
                String paramName = pickPropertyName(methodName);
                if (argLength == 0) {
                    // ignore getters defined in the Object.class
                    if (Object.class == eachMethod.getDeclaringClass())
                        continue;
                    getterContainer.add(ParameterGetter.newGetter(eachMethod, paramName));
                }
                else if (argLength == 1 && TypeInfo.isMap(targetType)) {
                    propertyGetter = ParameterGetter.newMapEntryGetter(eachMethod);
                }
            }

        }

        // create indexes
        for (ParameterSetter each : setterContainer)
            setterIndex.put(each.getCanonicalParameterName(), each);

        for (ParameterGetter each : getterContainer)
            getterIndex.put(each.getCanonicalParamName(), each);

    }

    private static boolean isBasicTypeOrObject(Class< ? > type) {
        return Object.class == type || TypeInfo.isBasicType(type);
    }

    private static void addNewSetter(List<ParameterSetter> setterContainer, String paramPart,
            Method m) {
        Class< ? >[] argTypes = m.getParameterTypes();
        if (argTypes.length != 1)
            return;

        assert (argTypes.length == 1);

        String paramName = getCanonicalParameterName(paramPart);
        if (paramName.length() <= 0) {
            // infer parameter name from argument type
            paramName = getCanonicalParameterName(argTypes[0].getSimpleName());
        }
        setterContainer.add(ParameterSetter.newSetter(argTypes[0], paramName, m));
        return;
    }

    static private Pattern propertyNamePattern = Pattern
            .compile("^(set|get|add|put|append)((\\S)(\\S*))?");
    static private Pattern pairedNamePattern = Pattern.compile("([A-Za-z0-9]*)_([A-Za-z0-9]*)");

    static String pickPropertyName(String methodName, boolean canonicalize) {
        Matcher m = null;
        m = propertyNamePattern.matcher(methodName);
        if (!m.matches())
            return null;
        else {
            if (m.group(2) != null)
                return canonicalize ? getCanonicalParameterName(m.group(2)) : m.group(2);
            else
                return "";
        }

    }

    static String pickPropertyName(String methodName) {
        Matcher m = null;
        m = propertyNamePattern.matcher(methodName);
        if (!m.matches())
            return null;
        else {
            if (m.group(2) != null)
                return m.group(2);
            else
                return "";
        }
    }

    static Pair<String, String> pickRelationName(String pairedName) {
        Matcher m = null;
        m = pairedNamePattern.matcher(pairedName);
        if (!m.matches())
            return null;
        else
            return new Pair<String, String>(getCanonicalParameterName(m.group(1)),
                    getCanonicalParameterName(m.group(2)));
    }

}
