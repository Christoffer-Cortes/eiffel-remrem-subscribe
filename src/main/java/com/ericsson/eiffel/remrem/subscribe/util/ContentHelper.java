package com.ericsson.eiffel.remrem.subscribe.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Helper class for content conversion
 */
@Component("contentHelper") @Slf4j public class ContentHelper {

    /**
     * get the content which can be correctly handled by most sse clients
     *
     * <p>
     *     remove the line breaks in the given content
     * </p>
     * @param content the original content
     * @return the content where line breaks are removed
     */
    public String getContent(String content) {
        return StringUtils.replace(StringUtils.replace(content, "\r", ""), "\n", "");
    }
}
