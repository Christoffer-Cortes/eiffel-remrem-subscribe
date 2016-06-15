package com.ericsson.eiffel.remrem.subscribe.util;

import org.junit.Assert;
import org.junit.Test;


public class ContentHelperTest {

    private ContentHelper unitUnderTest = new ContentHelper();

    public static final String INPUT_0 = "[{\n" + "  \"dsaf1measdfasfta\": {\n"
        + "    \"type\": \"EiffelActi\\\"\\\"vityStartedEvent\",\n" + "    \"version\": \"1.0\",\n"
        + "    \"time\": 1234567890,\n" + "    \"domainId\": \"example.domain\",\n"
        + "    \"id\": \"aaaaaaaa==========-bbbb-cccc-dddd-eeeeeeeeeee0^^^^\"\n" + "  },\n"
        + "  \"data\": {\n" + "    \"executionUri\": \"https://my.jenkins.host/myJob/43\",\n"
        + "    \"liveLogs\": [\n" + "      {\n" + "        \"name\": \"My build log\",\n"
        + "        \"uri\": \"file:///tmp/logs/data.log\"\n" + "      }\n" + "    ]\n" + "  },\n"
        + "  \"links\": {\n"
        + "    \"activityExecution\": \"aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeee1\",\n"
        + "    \"previousActivityExecution\": \"aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeee2\"\n" + "  }\n"
        + "}]\n";

    @Test public void testGetContent() throws Exception {
        String result = unitUnderTest.getContent(INPUT_0);
        Assert.assertFalse(result.contains("\n"));
        Assert.assertFalse(result.contains("\r"));
    }

}
