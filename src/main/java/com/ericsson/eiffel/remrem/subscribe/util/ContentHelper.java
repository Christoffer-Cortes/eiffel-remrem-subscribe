package com.ericsson.eiffel.remrem.subscribe.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component("contentHelper") @Slf4j public class ContentHelper {

    public String getContent(String content) {
        return StringUtils.replace(StringUtils.replace(content, "\r", " "), "\n", " ");
    }
}
