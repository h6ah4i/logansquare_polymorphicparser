package com.h6ah4i.logansquare_polymorphicparser.extension;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.base.ParserBase;
import com.fasterxml.jackson.core.io.IOContext;

import java.io.IOException;
import java.util.List;

public class TemporaryJsonParser extends ParserBase {
    private final List<Object> _parsedJson;
    private final int _size;
    private int _currPosition;
    private String _currText;
    private char[] _currTextChars;

    public TemporaryJsonParser(List<Object> parsedJson) {
        super(new IOContext(null, null, false), 0);

        // NOTE 1: parsedJson should be ArrayList for performance reason because this class is using #get() method.
        // NOTE 2: this class does not use iterator to avoid new objects creation for performance reason.
        _parsedJson = parsedJson;
        _size = parsedJson.size();
        _currPosition = -1;
    }

    @Override
    protected boolean loadMore() throws IOException {
        return (_currPosition < _size);
    }

    @Override
    protected void _finishString() throws IOException {
    }

    @Override
    protected void _closeInput() throws IOException {
    }

    @Override
    public ObjectCodec getCodec() {
        return null;
    }

    @Override
    public void setCodec(ObjectCodec c) {
        throw new IllegalStateException();
    }

    @Override
    public JsonToken nextToken() throws IOException {
        if (_currPosition >= (_size - 1)) {
            return JsonToken.NOT_AVAILABLE;
        }

        int pos = _currPosition + 1;
        JsonToken token = null;
        String text = null;

        for (; pos < _size; pos++) {
            Object obj = _parsedJson.get(pos);
            if (obj instanceof JsonToken) {
                token = (JsonToken) obj;
                break;
            } else {
                text = (String) obj;
            }
        }
        _currPosition = pos;
        _currToken = token;
        _currText = text;
        _currTextChars = null;

        if (_currToken != null) {
            switch (_currToken) {
                case NOT_AVAILABLE:
                case START_OBJECT:
                case END_OBJECT:
                case START_ARRAY:
                case END_ARRAY:
                    break;
                case FIELD_NAME:
                    _parsingContext.setCurrentName(_currText);
                    _textBuffer.resetWithString(_currText);
                    break;
                case VALUE_NUMBER_INT: {
                    boolean negative = _currText.startsWith("-");
                    int intLen = _currText.length() + (negative ? -1 : 0);

                    _parsingContext.setCurrentValue(_currText);
                    _textBuffer.resetWithString(_currText);
                    resetInt(negative, intLen);
                    break;
                }
                case VALUE_NUMBER_FLOAT: {
                    boolean negative = _currText.startsWith("-");
                    int len = _currText.length() + (negative ? -1 : 0);
                    int periodPos = _currText.indexOf('.');
                    int expPos = _currText.indexOf('e');
                    if (expPos < 0) {
                        expPos = _currText.indexOf('E');
                    }

                    int intLen;
                    int fractLen;
                    int expLen;

                    if (periodPos >= 0 && expPos >= 0) {
                        throw new JsonParseException("Illegal format number found: " + _currText, null);
                    }

                    if (periodPos >= 0) {
                        intLen = periodPos;
                        fractLen = 0;
                        expLen = len - periodPos;
                    } else if (expPos >= 0) {
                        intLen = expPos;
                        fractLen = 0;
                        expLen = len - expPos;
                    } else {
                        intLen = len;
                        fractLen = 0;
                        expLen = 0;
                    }

                    _parsingContext.setCurrentValue(_currText);
                    _textBuffer.resetWithString(_currText);
                    resetFloat(negative, intLen, fractLen, expLen);
                    break;
                }
                case VALUE_STRING:
                case VALUE_TRUE:
                case VALUE_FALSE:
                case VALUE_EMBEDDED_OBJECT:
                    _parsingContext.setCurrentValue(_currText);
                    _textBuffer.resetWithString(_currText);
                    break;
                case VALUE_NULL:
                    _parsingContext.setCurrentValue(null);
                    _textBuffer.resetWithEmpty();
                    break;
            }
        } else {
            _parsingContext.setCurrentValue(null);
            _textBuffer.resetWithEmpty();
        }

        return _currToken;
    }

    @Override
    public String getText() throws IOException {
        return _currText;
    }

    @Override
    public char[] getTextCharacters() throws IOException {
        if (_currTextChars == null && _currText != null) {
            // TODO optimization with using the getChars() method
            _currTextChars = _currText.toCharArray();
        }
        return _currTextChars;
    }

    @Override
    public int getTextLength() throws IOException {
        // TODO optimization with using the getChars() method
        return _currText.length();
    }

    @Override
    public int getTextOffset() throws IOException {
        // TODO optimization with using the getChars() method
        return 0;
    }

    @Override
    public byte[] getBinaryValue(Base64Variant b64variant) throws IOException {
        // TODO not implemented yet
        throw new IllegalStateException();
    }
}
