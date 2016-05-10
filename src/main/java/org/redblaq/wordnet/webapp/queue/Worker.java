package org.redblaq.wordnet.webapp.queue;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import org.redblaq.wordnet.domain.InputUtil;
import org.redblaq.wordnet.webapp.services.CacheService;
import org.redblaq.wordnet.webapp.services.ServiceProvider;
import org.redblaq.wordnet.webapp.util.Arguments;
import org.redblaq.wordnet.webapp.util.Responses;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Worker, which splits input into chunks and ssends them for processing to other workers.
 */
public class Worker extends HttpServlet {
    public static final String URL = "/worker";

    private final CacheService cacheService = ServiceProvider.INSTANCE.obtain(CacheService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String taskId = req.getParameter(Arguments.TASK_ID.toString());
        final String argument = req.getParameter(Arguments.ARGUMENT.toString());
        final Queue queue = QueueFactory.getDefaultQueue();

        cacheService.store(taskId, Responses.IN_PROGRESS.getText());

        final String[] inputChunks = InputUtil.splitIntoParts(argument);
        final String[] chunkIds = InputUtil.prepareIds(inputChunks.length);

        final String joinedChunkIds = InputUtil.join(chunkIds);
        cacheService.store(getSubTasksId(taskId), joinedChunkIds);

        int chunkOffset = 0;

        for (int i = 0; i < inputChunks.length; i++) {
            final String chunk = inputChunks[i];

            queue.add(TaskOptions.Builder
                    .withUrl(ChunkProcessorWorker.URL)
                    .param(Arguments.ARGUMENT.toString(), chunk)
                    .param(Arguments.CHUNK_TASK_ID.toString(), chunkIds[i])
                    .param(Arguments.CHUNK_OFFSET.toString(), String.valueOf(chunkOffset))
                    .param(Arguments.TASK_ID.toString(), taskId));

            chunkOffset += chunk.length() + InputUtil.WORDS_SEPARATOR.length();
        }
    }

    /* package */ static String getSubTasksId(String taskId) {
        return taskId + "-subtasks";
    }
}
