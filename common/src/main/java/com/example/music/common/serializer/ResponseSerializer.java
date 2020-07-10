package com.example.music.common.serializer;

import com.example.music.common.rep.HttpResponse;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ResponseSerializer extends StdSerializer<HttpResponse> {

    public ResponseSerializer() {
        super(HttpResponse.class);
    }

    @Override
    public void serialize(HttpResponse httpResponse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("msg",httpResponse.getMsg());
        jsonGenerator.writeNumberField("code",httpResponse.getCode());
        if (httpResponse.getPayload() != null) {
            jsonGenerator.writeObjectField("payload",httpResponse.getPayload());
        }
        jsonGenerator.writeEndObject();
    }
}
