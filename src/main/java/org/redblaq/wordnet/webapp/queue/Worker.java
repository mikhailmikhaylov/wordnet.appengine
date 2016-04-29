package org.redblaq.wordnet.webapp.queue;

import org.redblaq.wordnet.webapp.endpoints.Enqueue;
import org.redblaq.wordnet.webapp.services.CacheService;
import org.redblaq.wordnet.webapp.services.ServiceProvider;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Worker extends HttpServlet {
    private static final String TASK_IN_PROGRESS = "---IN-PROGRESS---";
    private final CacheService cacheService = ServiceProvider.INSTANCE.obtain(CacheService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String taskId = req.getParameter(Enqueue.TASK_ID);
        final String argument = req.getParameter(Enqueue.ARGUMENT);

        cacheService.store(taskId, TASK_IN_PROGRESS);

        final String result = ServiceProvider.INSTANCE.obtainProcessorService().process(argument);

        cacheService.store(taskId, result);
    }
}
