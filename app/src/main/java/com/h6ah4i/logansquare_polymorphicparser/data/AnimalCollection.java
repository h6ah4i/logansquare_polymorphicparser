package com.h6ah4i.logansquare_polymorphicparser.data;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class AnimalCollection {
    // NOTE: If LoganSquare adds polymorphic class handling,
    // no need to use AnimalBase class anymore (just use Animal class).
    @JsonField
    public List<AnimalBase> animals;
}
