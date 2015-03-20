package com.h6ah4i.logansquare_polymorphicparser.data;

import com.bluelinelabs.logansquare.JsonMapper;
import com.bluelinelabs.logansquare.LoganSquare;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.h6ah4i.logansquare_polymorphicparser.extension.PolymorphicParserHelper;

import java.io.IOException;

/**
 * Created by hasegawa on 3/20/15.
 */
public class AnimalBase$$JsonObjectMapper extends JsonMapper<AnimalBase> {
    @Override
    public AnimalBase parse(JsonParser jsonParser) throws IOException {
        return _parse(jsonParser);
    }

    @Override
    public void serialize(AnimalBase object, JsonGenerator generator, boolean writeStartAndEnd) throws IOException {
        _serialize(object, generator, writeStartAndEnd);
    }

    public static AnimalBase _parse(JsonParser jsonParser) throws IOException {
        if (jsonParser.getCurrentToken() == null) {
            jsonParser.nextToken();
        }
        if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            jsonParser.skipChildren();
            return null;
        }

        PolymorphicParserHelper helper = PolymorphicParserHelper.create(jsonParser, "name");
        final String subClassTypeId = helper.getSubClassTypeId();

        if ("cat".equals(subClassTypeId)) {
            return LoganSquare.mapperFor(Cat.class).parse(helper.getTmporaryParser());
        } else if ("dog".equals(subClassTypeId)) {
            return LoganSquare.mapperFor(Dog.class).parse(helper.getTmporaryParser());
        } else if ("test".equals(subClassTypeId)) {
            return LoganSquare.mapperFor(TestAnimal.class).parse(helper.getTmporaryParser());
        } else {
            // corresponding sub class is not found
            return null;
        }
    }

    public static void _serialize(AnimalBase object, JsonGenerator jsonGenerator, boolean writeStartAndEnd) throws IOException {
        if (writeStartAndEnd) {
            jsonGenerator.writeStartObject();
        }

        JsonMapper mapper = LoganSquare.mapperFor(object.getClass());
        mapper.serialize(object, jsonGenerator, true);

        if (writeStartAndEnd) {
            jsonGenerator.writeEndObject();
        }
    }
}
