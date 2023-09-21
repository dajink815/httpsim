package com.uangel.sim.scenario.nodes;

import com.uangel.sim.scenario.type.FieldType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dajin kim
 */
@Getter
@Setter
@AllArgsConstructor
public class FieldNode {
    private String name;
    private FieldType type;
    private String value;

    @Override
    public String toString() {
        return name + "(" + type + "):" + value;
    }
}
