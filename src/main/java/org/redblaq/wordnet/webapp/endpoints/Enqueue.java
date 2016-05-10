package org.redblaq.wordnet.webapp.endpoints;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import org.redblaq.wordnet.webapp.queue.Worker;
import org.redblaq.wordnet.webapp.services.ChannelService;
import org.redblaq.wordnet.webapp.services.ServiceProvider;
import org.redblaq.wordnet.webapp.util.Arguments;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import static org.redblaq.wordnet.webapp.util.ServletHelper.respondRaw;

// https://cloud.google.com/appengine/docs/java/taskqueue/overview-push

/**
 * Push queue endpoint.
 * <p>Enqueues basic worker task.
 */
public class Enqueue extends HttpServlet {

    private final ChannelService channelService = ServiceProvider.obtainService(ChannelService.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String requestArgument = request.getParameter(Arguments.ARGUMENT.toString());
        final String channelId = request.getParameter(Arguments.CHANNEL_ID.toString());
        validateArgument(requestArgument);
        final Queue queue = QueueFactory.getDefaultQueue();

        final String taskId = UUID.randomUUID().toString();

        if (channelId != null && !channelId.isEmpty()) {
            channelService.registerTask(taskId, channelId);
        }

        queue.add(TaskOptions.Builder
                .withUrl(Worker.URL)
                .param(Arguments.ARGUMENT.toString(), requestArgument)
                .param(Arguments.TASK_ID.toString(), taskId));

        respondRaw(response, taskId);
    }

    private void validateArgument(String argument) {
        if (argument == null || argument.isEmpty()) {
            throw new IllegalArgumentException("Null argument");
        }
    }
}
