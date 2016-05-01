package org.redblaq.wordnet.webapp;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
