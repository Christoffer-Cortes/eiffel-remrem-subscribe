package com.ericsson.eiffel.remrem.subscribe.sse;


import com.ericsson.eiffel.remrem.subscribe.message.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
public class EiffelEmitter {
    @Autowired
    private Sender rabbitSender;

    @RequestMapping(path = "/stream", method = RequestMethod.GET)
    public SseEmitter eiffelSubscriber(@RequestParam("bindingKey") String bindingKey)
        throws IOException {
        System.out.println(bindingKey);
        SseEmitter emitter = new SseEmitter(1000000L);
        System.out.println("Timeout is" + emitter.getTimeout());
        emitter.onCompletion(() -> System.out.println("completed"));
        rabbitSender.addEmitter(emitter, bindingKey );
        return emitter;
    }
}
