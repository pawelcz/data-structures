package com.pczyz.test;


import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Attribute bikeAttributeLength = new Attribute("Length", "2.5m", null);

        List<Attribute> bikeDimensions = new ArrayList<>();
        bikeDimensions.add(bikeAttributeLength);

        Attribute bikeDimensionsAttributeGroup = new Attribute("Dimensions", null, bikeDimensions);

        Attribute bikeAttributeWheelSize = new Attribute("Wheel Size", "28\"", null);
        Attribute bikeAttributeFrameSize = new Attribute("Frame Size", "18\"", null);

        List<Attribute> bikeSizes = new ArrayList<>();
        bikeSizes.add(bikeAttributeWheelSize);
        bikeSizes.add(bikeAttributeFrameSize);
        bikeSizes.add(bikeDimensionsAttributeGroup);

        Attribute bikeSizesAttributeGroup = new Attribute("Sizes", null, bikeSizes);

        Attribute bikeAttributePrice = new Attribute("Price", "2000.00", null);
        Attribute bikeAttributeType = new Attribute("Type", "MTB", null);

        List<Attribute> bikeAttributes = new ArrayList<>();
        bikeAttributes.add(bikeAttributePrice);
        bikeAttributes.add(bikeAttributeType);
        bikeAttributes.add(bikeSizesAttributeGroup);

        Product bike = new Product("Bike", bikeAttributes);

        System.out.println(bike.printProductAttributes());
    }
}
