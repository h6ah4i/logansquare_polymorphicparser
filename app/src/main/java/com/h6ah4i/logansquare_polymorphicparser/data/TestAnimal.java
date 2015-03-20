package com.h6ah4i.logansquare_polymorphicparser.data;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class TestAnimal extends Animal {
    @JsonField
    String f_string;

    @JsonField
    int f_int;

    @JsonField
    long f_long;

    @JsonField
    float f_float;

    @JsonField
    double f_double;

    @JsonField
    TestObject f_object;

    @JsonField
    List<String> list_string;

    @JsonField
    List<Integer> list_int;

    @JsonField
    List<Long> list_long;

    @JsonField
    List<Float> list_float;

    @JsonField
    List<Double> list_double;

    @JsonField
    List<TestObject> list_object;
}
