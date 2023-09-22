package com.uangel.sim.scenario.type;


import com.uangel.sim.util.StringUtil;

/**
 * @author dajin kim
 */
public enum FieldType {
    STR("str"), INT("int"), LONG("long"), BOOL("bool"), ARRAY("arr");

    private final String value;

    FieldType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static FieldType getTypeEnum(String type) {
        switch (StringUtil.blankIfNull(type).toLowerCase()) {
            case "int":
            case "integer":
            case "i":
                return INT;
            case "long":
            case "l":
                return LONG;
            case "bool":
            case "boolean":
            case "b":
                return BOOL;
            case "arr":
            case "array":
            case "a":
                return ARRAY;
            case "str":
            case "string":
            case "s":
            default:
                return STR;
        }
    }
}
