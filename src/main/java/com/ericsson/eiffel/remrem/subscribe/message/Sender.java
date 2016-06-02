package com.ericsson.eiffel.remrem.subscribe.message;


import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public interface Sender {
    void addEmitter(SseEmitter sseEmitter, String bindingKey) throws IOException;
}
