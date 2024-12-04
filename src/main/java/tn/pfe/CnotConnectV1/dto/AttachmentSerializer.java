package tn.pfe.CnotConnectV1.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import tn.pfe.CnotConnectV1.entities.Attachment;

import java.io.IOException;
import java.util.Base64;

public class AttachmentSerializer extends JsonSerializer<Attachment> {

    @Override
    public void serialize(Attachment attachment, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", attachment.getId());
        gen.writeStringField("fileName", attachment.getFileName());
        gen.writeStringField("fileType", attachment.getFileType());
        gen.writeStringField("data", Base64.getEncoder().encodeToString(attachment.getData()));  
        gen.writeEndObject();
    }
}