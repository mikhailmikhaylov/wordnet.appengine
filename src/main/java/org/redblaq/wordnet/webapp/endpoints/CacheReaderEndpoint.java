package org.redblaq.wordnet.webapp.endpoints;

import com.google.appengine.repackaged.com.google.api.client.util.Strings;
import org.redblaq.wordnet.webapp.services.CompositionRoot;
import org.redblaq.wordnet.webapp.util.Arguments;
import org.redblaq.wordnet.webapp.util.Responses;
import org.redblaq.wordnet.webapp.util.ServletHelper;
import org.redblaq.wordnet.webapp.services.CacheService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet endpoint, which retrieves information from MemCache and returns it in raw format.
 * <p>At this point, we use MemCache to deliver processing results.
 */
public class CacheReaderEndpoint extends HttpServlet {

    private final CacheService cacheService = CompositionRoot.INSTANCE.resolve(CacheService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        final String taskId = req.getParameter(Arguments.TASK_ID.toString());

        final String cachedValue = cacheService.retrieve(taskId);
        final String output = Strings.isNullOrEmpty(cachedValue)
                ? Responses.TASK_NOT_FOUND.getText()
                : cachedValue;

        ServletHelper.respondRaw(resp, output);
    }
}
