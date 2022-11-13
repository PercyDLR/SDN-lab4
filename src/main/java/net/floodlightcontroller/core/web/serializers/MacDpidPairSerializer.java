package net.floodlightcontroller.core.web.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.floodlightcontroller.core.types.MacDpidPair;

import java.io.IOException;

public class MacDpidPairSerializer extends JsonSerializer<MacDpidPair> {
    @Override
    public void serialize(MacDpidPair entry, JsonGenerator jGen, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

        jGen.writeStartObject();
        jGen.writeStringField("MAC", entry.getMac());

        jGen.writeFieldName("switch");
        jGen.writeStartObject();
        jGen.writeStringField("dpid", entry.getDpid());
        jGen.writeEndObject();

        jGen.writeEndObject();
    }
}
