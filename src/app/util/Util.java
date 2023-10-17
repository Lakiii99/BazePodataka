package app.util;

import app.tree.model.AttributeType;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {

    private static final Map<AttributeType, Class> attributeTypesMap;

    static {
        attributeTypesMap = new HashMap<>();
        attributeTypesMap.put(AttributeType.BIGINT, Long.class);
        attributeTypesMap.put(AttributeType.BIT, Boolean.class);
        attributeTypesMap.put(AttributeType.CHAR, String.class);
        attributeTypesMap.put(AttributeType.DATE, LocalDate.class);
        attributeTypesMap.put(AttributeType.DATETIME, Timestamp.class);
        attributeTypesMap.put(AttributeType.DECIMAL, BigDecimal.class);
        attributeTypesMap.put(AttributeType.FLOAT, Double.class);
        attributeTypesMap.put(AttributeType.INT, Integer.class);
        attributeTypesMap.put(AttributeType.IMAGE, Byte[].class);
        attributeTypesMap.put(AttributeType.NUMERIC, BigDecimal.class);
        attributeTypesMap.put(AttributeType.NVARCHAR, String.class);
        attributeTypesMap.put(AttributeType.REAL, Float.class);
        attributeTypesMap.put(AttributeType.SMALLINT, Short.class);
        attributeTypesMap.put(AttributeType.TEXT, String.class);
        attributeTypesMap.put(AttributeType.TIME, Time.class);
        attributeTypesMap.put(AttributeType.VARCHAR, String.class);
    }

    private static final List<AttributeType> numericAttributeTypes;

    static {
        numericAttributeTypes = new ArrayList<>();
        numericAttributeTypes.add(AttributeType.BIGINT);
        numericAttributeTypes.add(AttributeType.DECIMAL);
        numericAttributeTypes.add(AttributeType.FLOAT);
        numericAttributeTypes.add(AttributeType.INT);
        numericAttributeTypes.add(AttributeType.NUMERIC);
        numericAttributeTypes.add(AttributeType.REAL);
        numericAttributeTypes.add(AttributeType.SMALLINT);
    }

    public static Class getClassForAttributeType(final AttributeType attributeType) {
        if (attributeTypesMap.containsKey(attributeType)) {
            return attributeTypesMap.get(attributeType);
        }
        return Object.class;
    }

    public static boolean checkIfAttributeTypeIsNumeric(final AttributeType attributeType) {
        return numericAttributeTypes.contains(attributeType);
    }
}