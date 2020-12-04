package com.pczyz.test;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class Attribute {

    private final String name;
    private final String value;
    private final List<Attribute> attributes;

    public Attribute(String name, String value, List<Attribute> attributes) {
        this.name = name;
        this.value = value;
        this.attributes = attributes;
    }

    public static String printAttributes(List<Attribute> attributes) {
        return attributes.stream()
                .map(Attribute::printAttribute)
                .collect(joining("\n"));
    }

    private String printAttribute() {
        if (value != null && attributes == null) {
            return name.concat(": ").concat(value);
        } else if (attributes != null) {
            return String.join("\n",
                    "------ Attribute Group: ".concat(name).concat(" -----------------"),
                    printAttributes(attributes),
                    "------ End of Attribute Group: ".concat(name).concat(" ----------"));
        }
        return "";
    }
}
