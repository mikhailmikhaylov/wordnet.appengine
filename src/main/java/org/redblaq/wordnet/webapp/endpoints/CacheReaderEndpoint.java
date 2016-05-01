package org.redblaq.wordnet.webapp.endpoints;

import com.google.appengine.repackaged.com.google.api.client.util.Strings;
import org.redblaq.wordnet.webapp.ServletHelper;
import org.redblaq.wordnet.webapp.services.ServiceProvider;
import org.redblaq.wordnet.webapp.services.CacheService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CacheReaderEndpoint extends HttpServlet {

    private static final String TASK_ID_ARG = "task-id";
    private static final String TASK_NOT_FOUND_RESPONSE = "---NOT-FOUND---";

    private final CacheService cacheService = ServiceProvider.INSTANCE.obtain(CacheService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String taskId = req.getParameter(TASK_ID_ARG);

        final String cachedValue = cacheService.retrieve(taskId);
        final String output = Strings.isNullOrEmpty(cachedValue) ? TASK_NOT_FOUND_RESPONSE : cachedValue;

        ServletHelper.respondRaw(resp, output);
    }
}
