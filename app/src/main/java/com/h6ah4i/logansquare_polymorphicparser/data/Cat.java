package com.h6ah4i.logansquare_polymorphicparser.data;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class Cat extends Animal {
    @JsonField(name = "coat_color")
    String coatColor;
}
