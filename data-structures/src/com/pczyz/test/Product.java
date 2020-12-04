package com.pczyz.test;

import java.util.List;

import static com.pczyz.test.Attribute.printAttributes;

public class Product {

    private final String productName;
    private final List<Attribute> attributes;

    public Product(String productName, List<Attribute> attributes) {
        this.productName = productName;
        this.attributes = attributes;
    }

    public String printProductAttributes() {
        return String.join("\n",
                "*** ".concat(productName).concat(" attributes ******************"),
                printAttributes(attributes),
                "*** End of ".concat(productName).concat(" attributes **********"));
    }

}
