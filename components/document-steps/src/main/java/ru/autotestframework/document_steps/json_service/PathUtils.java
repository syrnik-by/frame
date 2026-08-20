package ru.autotestframework.document_steps.json_service;

import java.util.List;
import java.util.regex.Pattern;

class PathUtils {
    private static final Pattern ENCODED_TILDA_PATTERN = Pattern.compile("~");
    private static final Pattern ENCODED_SLASH_PATTERN = Pattern.compile("/");

    private static final Pattern DECODED_TILDA_PATTERN = Pattern.compile("~0");
    private static final Pattern DECODED_SLASH_PATTERN = Pattern.compile("~1");

    private static String encodePath(Object object) {
        String path = object.toString(); // see http://tools.ietf.org/html/rfc6901#section-4
        path = ENCODED_TILDA_PATTERN.matcher(path).replaceAll("~0");
        return ENCODED_SLASH_PATTERN.matcher(path).replaceAll("~1");
    }

    private static String decodePath(Object object) {
        String path = object.toString(); // see http://tools.ietf.org/html/rfc6901#section-4
        path = DECODED_TILDA_PATTERN.matcher(path).replaceAll("~");
        return DECODED_SLASH_PATTERN.matcher(path).replaceAll("/");
    }

    /**
     * converts json path to string
     * @param path path
     * @return
     * @param <T>
     */
    static <T> String getPathRepresentation(List<T> path) {
        StringBuilder builder = new StringBuilder();
        builder.append('/');
        int count = 0;
        for (Object o : path) {
            if (++count > 1) builder.append('/');
            builder.append(encodePath(o));
        }
        return builder.toString();
    }
}
