package com.h6ah4i.logansquare_polymorphicparser.data;


import com.bluelinelabs.logansquare.typeconverters.TypeConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

/**
 * Created by hasegawa on 3/20/15.
 */
public class AnimalBaseTypeConverter implements TypeConverter<AnimalBase> {
    @Override
    public AnimalBase parse(JsonParser jsonParser) throws IOException {
        return AnimalBase$$JsonObjectMapper._parse(jsonParser);
    }

    @Override
    public void serialize(AnimalBase object, String fieldName, boolean writeFieldNameForObject, JsonGenerator jsonGenerator) throws IOException {
        if (writeFieldNameForObject) {
            jsonGenerator.writeFieldName(fieldName);
        }
        AnimalBase$$JsonObjectMapper._serialize(object, jsonGenerator, writeFieldNameForObject);
    }
}
