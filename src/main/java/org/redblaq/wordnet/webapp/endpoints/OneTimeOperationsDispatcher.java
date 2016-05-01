package org.redblaq.wordnet.webapp.endpoints;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import org.redblaq.wordnet.domain.BaseWordsStore;
import org.redblaq.wordnet.domain.InputUtil;
import org.redblaq.wordnet.webapp.queue.SystemWorker;
import org.redblaq.wordnet.webapp.util.Arguments;
import org.redblaq.wordnet.webapp.util.Responses;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static org.redblaq.wordnet.webapp.util.ServletHelper.respondRaw;

public class OneTimeOperationsDispatcher extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String operationName = req.getParameter(Arguments.OPERATION.toString());
        final Function<HttpServletRequest, String> operation = operations.get(operationName);
        if (operation == null) {
            respondRaw(resp, Responses.UNKNOWN_OPERATION.getText());
            return;
        }
        final String operationResult = operation.apply(req);
        respondRaw(resp, operationResult);
    }

    private static final ImmutableMap<String, Function<HttpServletRequest, String>> operations =
            new ImmutableMap.Builder<String, Function<HttpServletRequest, String>>()
                    .put(ProcessBaseWords.class.getSimpleName(), new ProcessBaseWords())
                    .build();

    private static class ProcessBaseWords implements Function<HttpServletRequest, String> {
        private static final int CHUNK_SIZE = 100;

        @Override
        public String apply(HttpServletRequest ignored) {
            final String baseWordsRaw = BaseWordsStore.getRaw();
            final String[] wordsRaw = baseWordsRaw.split(InputUtil.WORDS_SEPARATOR);

            final int baseWordsLength = wordsRaw.length;
            final int fullChunksQuantity = baseWordsLength / CHUNK_SIZE;
            final int lastChunkSize = baseWordsLength % CHUNK_SIZE;

            for (int i = 0; i < fullChunksQuantity; i++) {
                final int from = i * CHUNK_SIZE;
                final int to = (i + 1) * CHUNK_SIZE;
                final String[] chunk = Arrays.copyOfRange(wordsRaw, from, to);
                enqueueChunkWrite(chunk);
            }
            if (lastChunkSize > 0) {
                final int from = baseWordsLength - lastChunkSize;
                final String[] chunk = Arrays.copyOfRange(wordsRaw, from, baseWordsLength);
                enqueueChunkWrite(chunk);
            }

            return Responses.CHUNKS_ENQUEUED.getText();
        }

        private void enqueueChunkWrite(String[] wordsRawChunk) {
            final Queue queue = QueueFactory.getDefaultQueue();
            final String argumentValue = InputUtil.join(wordsRawChunk, InputUtil.WORDS_SEPARATOR);
            queue.add(TaskOptions.Builder
                    .withUrl(SystemWorker.URL)
                    .param(Arguments.OPERATION.toString(),
                            SystemWorker.StoreBaseWordsChunk.class.getSimpleName())
                    .param(Arguments.ARGUMENT.toString(), argumentValue));
        }
    }
}
