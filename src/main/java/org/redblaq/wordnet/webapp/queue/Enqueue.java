package org.redblaq.wordnet.webapp.queue;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

// https://cloud.google.com/appengine/docs/java/taskqueue/overview-push
public class Enqueue extends HttpServlet {

    /* package */ static final String ARGUMENT = "enqueue-arg";
    /* package */ static final String TASK_ID = "task-id";
    private static final String WORKER_URL = "/worker";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String requestArgument = request.getParameter(ARGUMENT);
        validateArgument(requestArgument);
        final Queue queue = QueueFactory.getDefaultQueue();

        final String taskId = UUID.randomUUID().toString();

        queue.add(TaskOptions.Builder
                .withUrl(WORKER_URL)
                .param(ARGUMENT, requestArgument)
                .param(TASK_ID, taskId));

        respondRaw(response, taskId);
    }

    private void respondRaw(HttpServletResponse response, String value) throws IOException {
        final PrintWriter responseWriter = response.getWriter();
        responseWriter.write(value);
        responseWriter.close();
    }

    private void validateArgument(String argument) {
        if (argument == null || argument.isEmpty()) {
            throw new IllegalArgumentException("Null argument");
        }
    }
}
