package com.h6ah4i.logansquare_polymorphicparser.extension;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PolymorphicParserHelper {
    private final String _subClassTypeIdFieldName;
    private final String _subClassTypeId;
    private final TemporaryJsonParser _tmpParser;

    public String getSubClassTypeIdFieldName() {
        return _subClassTypeIdFieldName;
    }

    public String getSubClassTypeId() {
        return _subClassTypeId;
    }

    public TemporaryJsonParser getTmporaryParser() {
        return _tmpParser;
    }

    private PolymorphicParserHelper(String subClassTypeIdFieldName, String subClassTypeId, TemporaryJsonParser tmpParser) {
        _subClassTypeIdFieldName = subClassTypeIdFieldName;
        _subClassTypeId = subClassTypeId;
        _tmpParser = tmpParser;
    }

    public static PolymorphicParserHelper create(JsonParser jsonParser, String subClassTypeIdFieldName) throws IOException {
        List<Object> parsedJson = new ArrayList<>();

        String subClassTypeId = null;
        int level = 1;
        JsonToken token = jsonParser.getCurrentToken();
        parsedJson.add(token);

        while (level > 0) {
            JsonToken nextToken = jsonParser.nextToken();

            switch (nextToken) {
                case NOT_AVAILABLE:
                    break;
                case START_OBJECT:
                    level += 1;
                    break;
                case END_OBJECT:
                    level -= 1;
                    break;
                case START_ARRAY:
                    level += 1;
                    break;
                case END_ARRAY:
                    level -= 1;
                    break;
                case FIELD_NAME: {
                    // field name
                    String fieldName = jsonParser.getText();
                    parsedJson.add(fieldName);
                    break;
                }
                case VALUE_EMBEDDED_OBJECT: {
                    String obj = jsonParser.getText();
                    parsedJson.add(obj);
                    break;
                }
                case VALUE_STRING: {
                    String obj = jsonParser.getText();
                    parsedJson.add(obj);

                    if (subClassTypeIdFieldName.equals(jsonParser.getCurrentName())) {
                        subClassTypeId = obj;
                    }
                    break;
                }
                case VALUE_NUMBER_INT: {
                    String obj = jsonParser.getText();
                    parsedJson.add(obj);
                    break;
                }
                case VALUE_NUMBER_FLOAT: {
                    String obj = jsonParser.getText();
                    parsedJson.add(obj);
                    break;
                }
                case VALUE_TRUE: {
                    String obj = jsonParser.getText();
                    parsedJson.add(obj);
                    break;
                }
                case VALUE_FALSE: {
                    String obj = jsonParser.getText();
                    parsedJson.add(obj);
                    break;
                }
                case VALUE_NULL: {
                    String obj = jsonParser.getText();
                    parsedJson.add(obj);
                    break;
                }
            }

            parsedJson.add(nextToken);
        }

        return new PolymorphicParserHelper(subClassTypeIdFieldName, subClassTypeId, new TemporaryJsonParser(parsedJson));
    }
}
