package org.enpal.appointmentbooking.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule()
                        .addSerializer(OffsetDateTime.class, new JsonSerializer<>() {
                            @Override
                            public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                                gen.writeString(value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")));
                            }
                        }))
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
