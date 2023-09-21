package com.uangel.sim.scenario.type;

import com.uangel.sim.util.StringUtil;

/**
 * @author dajin kim
 */
public enum NodeName {
    SCENARIO("scenario"),
    MSG("msg"), HEADER("header"), BODY("body"), FIELD("field"),
    STRUCT("struct");

    private final String value;

    NodeName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static NodeName getNodeName(String type) {
        switch (StringUtil.blankIfNull(type).toLowerCase()) {
            case "scenario":
                return SCENARIO;
            case "header":
            case "h":
                return HEADER;
            case "body":
            case "b":
                return BODY;
            case "field":
            case "f":
            default:
                return FIELD;
        }
    }
}
