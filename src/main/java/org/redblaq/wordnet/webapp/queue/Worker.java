package org.redblaq.wordnet.webapp.queue;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import org.redblaq.wordnet.domain.InputUtil;
import org.redblaq.wordnet.webapp.services.CacheService;
import org.redblaq.wordnet.webapp.services.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.redblaq.wordnet.webapp.endpoints.Enqueue.ARGUMENT;
import static org.redblaq.wordnet.webapp.endpoints.Enqueue.TASK_ID;

public class Worker extends HttpServlet {
    /* package */ static final String TASK_IN_PROGRESS = "---IN-PROGRESS---";
    /* package */ static final String SUBTASK_ID = "subtask-id";
    /* package */ static final String CHUNK_OFFSET = "chunk-offset";
    private final CacheService cacheService = ServiceProvider.INSTANCE.obtain(CacheService.class);
    private final Logger log = LoggerFactory.getLogger(Worker.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String taskId = req.getParameter(TASK_ID);
        final String argument = req.getParameter(ARGUMENT);
        final Queue queue = QueueFactory.getDefaultQueue();

        cacheService.store(taskId, TASK_IN_PROGRESS);

        final String[] inputChunks = InputUtil.splitIntoParts(argument);
        final String[] chunkIds = InputUtil.prepareIds(inputChunks.length);

        final String joinedChunkIds = InputUtil.join(chunkIds);
        cacheService.store(getSubtasksId(taskId), joinedChunkIds);

        int chunkOffset = 0;

        for (int i = 0; i < inputChunks.length; i++) {
            final String chunk = inputChunks[i];

            logI("---CHUNK: " + chunk);

            queue.add(TaskOptions.Builder
                    .withUrl(ChunkProcessorWorker.URL)
                    .param(ARGUMENT, chunk)
                    .param(SUBTASK_ID, chunkIds[i])
                    .param(CHUNK_OFFSET, String.valueOf(chunkOffset))
                    .param(TASK_ID, taskId));

            chunkOffset += chunk.length() + InputUtil.WORDS_SEPARATOR.length();
        }
    }

    private void logI(String message) {
        log.info(message);
    }

    /* package */ static String getSubtasksId(String taskId) {
        return taskId + "-subtasks";
    }
}
