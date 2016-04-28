package org.redblaq.wordnet.webapp.queue;

import org.redblaq.wordnet.webapp.CacheService;
import org.redblaq.wordnet.webapp.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Worker extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(Worker.class);
    private final CacheService cacheService = ServiceProvider.INSTANCE.obtain(CacheService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String taskId = req.getParameter(Enqueue.TASK_ID);
        final String argument = req.getParameter(Enqueue.ARGUMENT);

        logI("----- " + taskId + " : " + argument);
        cacheService.store(taskId, "---IN-PROGRESS---");

        final String result = ServiceProvider.INSTANCE.obtainProcessorService().process(argument);
        logI("----- " + taskId + " : " + result);

        cacheService.store(taskId, result);
    }

    private void logI(String message) {
        if (log.isInfoEnabled()) {
            log.info(message);
        }
    }
}
