package org.redblaq.wordnet.webapp;

import com.google.appengine.repackaged.com.google.api.client.util.Strings;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CacheReaderEndpoint extends HttpServlet {

    private static final String TASK_ID_ARG = "task-id";
    private static final String TEXT_CONTENT_TYPE = "text/html";

    private final CacheService cacheService = ServiceProvider.INSTANCE.obtain(CacheService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String taskId = req.getParameter(TASK_ID_ARG);

        final String cachedValue = cacheService.retrieve(taskId);
        final String output = Strings.isNullOrEmpty(cachedValue) ? "---NOT-FOUND---" : cachedValue;

        respond(resp, output);
    }

    private static void respond(HttpServletResponse response, String output) throws IOException {
        response.setContentType(TEXT_CONTENT_TYPE);
        response.getWriter().write(output);
    }
}
