package com.uangel.sim.scenario.type;

/**
 * @author dajin kim
 */
public enum AttrName {
    NAME("name"),
    METHOD("method"), URL("url"),
    STATUS("status"),
    TYPE("type"), VALUE("value");

    private final String value;

    AttrName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
