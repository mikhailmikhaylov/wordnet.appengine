package org.redblaq.wordnet.webapp.queue;

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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String taskId = req.getParameter(Enqueue.TASK_ID);
        final String argument = req.getParameter(Enqueue.ARGUMENT);

        logI(taskId + " : " + argument);
        // PROCESS AND RESPOND

        final String result = ServiceProvider.INSTANCE.obtainProcessorService().process(argument);
        logI(taskId + " : " + result);
    }

    private void logI(String message) {
        if (log.isInfoEnabled()) {
            log.info(message);
        }
    }
}
