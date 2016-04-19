package org.redblaq.wordnet.webapp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProcessorEndpoint extends HttpServlet {

    private static final String INPUT_ATTRIBUTE_NAME = "input";

    private static final String TEXT_CONTENT_TYPE = "text/html";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        final String input = extractInput(request);
        // Hello and welcome to appengine! Minute -> crash
        final String output = ServiceProvider.INSTANCE.obtainProcessorService().process(input);

        response.setContentType(TEXT_CONTENT_TYPE);
        response.getWriter().write(output); // wow such style
    }

    private static String extractInput(HttpServletRequest request) {
        return request.getParameter(INPUT_ATTRIBUTE_NAME);
    }
}
