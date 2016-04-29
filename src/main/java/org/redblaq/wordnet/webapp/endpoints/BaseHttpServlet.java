package org.redblaq.wordnet.webapp.endpoints;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Common base for all endpoints. Will be changed or eliminated later.
 */
public abstract class BaseHttpServlet extends HttpServlet {

    private static final String TEXT_CONTENT_TYPE = "text/html";

    /* package */ void respondRaw(HttpServletResponse response, String output) throws IOException {
        response.setContentType(TEXT_CONTENT_TYPE);
        response.getWriter().write(output);
    }
}
