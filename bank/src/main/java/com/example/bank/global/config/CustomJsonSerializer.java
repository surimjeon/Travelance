package com.example.bank.global.config;

import org.springframework.kafka.support.serializer.JsonSerializer;

public class CustomJsonSerializer<T> extends JsonSerializer<T> {
    public CustomJsonSerializer() {
        super();
        this.addTypeInfo = false;
    }
}