package com.ericsson.eiffel.remrem.subscribe.sse;


import com.ericsson.eiffel.remrem.subscribe.message.Sender;
import com.ericsson.eiffel.remrem.subscribe.util.ConnectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController public class EiffelEmitter {
    @Autowired private Sender rabbitSender;
    @Autowired private ConnectionHelper connectionHelper;

    @RequestMapping(path = "/stream", method = RequestMethod.GET)
    public SseEmitter eiffelSubscriber(@RequestParam("bindingKey") String bindingKey)
        throws IOException {
        System.out.println(bindingKey);
        // TODO investigate what timeout means exactly
        SseEmitter emitter = new SseEmitter(100000000L);
        connectionHelper.onEmitterStarted(emitter);
        System.out.println("Timeout is" + emitter.getTimeout());
        emitter.onCompletion(() -> {
            System.out.println("completed");
            connectionHelper.onEmitterCompleted(emitter);
        });
        rabbitSender.addEmitter(emitter, bindingKey);
        return emitter;
    }
}
