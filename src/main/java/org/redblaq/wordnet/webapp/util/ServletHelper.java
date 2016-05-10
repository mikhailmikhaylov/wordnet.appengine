package org.redblaq.wordnet.webapp.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Utility helper for common servle-specific operations.
 */
public class ServletHelper {

    private static final String TEXT_CONTENT_TYPE = "text/html";

    private ServletHelper() {
    }

    /**
     * Prints String message into {@link HttpServletResponse}.
     */
    public static void respondRaw(HttpServletResponse response, String message) throws IOException {
        response.setContentType(TEXT_CONTENT_TYPE);
        response.getWriter().write(message);
    }
}
