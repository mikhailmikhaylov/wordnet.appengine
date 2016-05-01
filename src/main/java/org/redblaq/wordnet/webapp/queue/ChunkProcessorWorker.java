package org.redblaq.wordnet.webapp.queue;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.redblaq.wordnet.domain.InputUtil;
import org.redblaq.wordnet.domain.entities.TextEntry;
import org.redblaq.wordnet.domain.entities.dto.ResponseDto;
import org.redblaq.wordnet.webapp.services.CacheService;
import org.redblaq.wordnet.webapp.services.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.redblaq.wordnet.webapp.endpoints.Enqueue.ARGUMENT;
import static org.redblaq.wordnet.webapp.endpoints.Enqueue.TASK_ID;

public class ChunkProcessorWorker extends HttpServlet {

    private static final String PROCESSING_ERROR = "---ERROR---";
    /* package */ static final String URL = "/worker/chunk";
    private final CacheService cacheService = ServiceProvider.INSTANCE.obtain(CacheService.class);
    private final Logger log = LoggerFactory.getLogger(ChunkProcessorWorker.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String taskId = req.getParameter(TASK_ID);
        final String subtaskId = req.getParameter(Worker.SUBTASK_ID);
        final String argument = req.getParameter(ARGUMENT);
        final int chunkOffset = Integer.parseInt(req.getParameter(Worker.CHUNK_OFFSET));

        cacheService.store(subtaskId, Worker.TASK_IN_PROGRESS);

        final List<TextEntry> textEntries = ServiceProvider.INSTANCE
                .obtainProcessorService().processText(argument, chunkOffset);

        final ResponseDto result = ResponseDto.of(textEntries);

        final ObjectMapper mapper = new ObjectMapper();
        final String jsonResult = mapper.writeValueAsString(result);

        logI("--------- " + jsonResult);

        cacheService.store(subtaskId, jsonResult);

        collectResult(taskId);
    }

    private void collectResult(String taskId) {
        final String subtasksId = Worker.getSubtasksId(taskId);
        final String joinedSubtasks = cacheService.retrieve(subtasksId);

        final String[] subtaskIds = InputUtil.split(joinedSubtasks);
        final String[] subtaskResults = new String[subtaskIds.length];

        boolean shouldCollectResult = true;

        for (int i = 0; i < subtaskIds.length; i++) {
            final String subtaskId = subtaskIds[i];
            final String subtaskResult = cacheService.retrieve(subtaskId);
            subtaskResults[i] = subtaskResult;

            logI(subtaskResult);

            shouldCollectResult = !isTaskInProgress(subtaskResult);
            if (!shouldCollectResult) {
                break;
            }
        }

        if (!shouldCollectResult) {
            return;
        }

        try {
            final ResponseDto responseDto = readResults(subtaskResults);
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonResult = mapper.writeValueAsString(responseDto);

            cacheService.store(taskId, jsonResult);
        } catch (IOException e) {
            logE(e);
            cacheService.store(taskId, PROCESSING_ERROR);
        }
    }

    private ResponseDto readResults(String[] results) throws IOException {
        final ObjectReader reader = new ObjectMapper().reader(ResponseDto.class);
        final ResponseDto[] responses = new ResponseDto[results.length];
        for (int i = 0; i < results.length; i++) {
            final String result = results[i];
            final ResponseDto response = reader.readValue(result);
            responses[i] = response;
        }
        return ResponseDto.merge(responses);
    }

    private void logI(String message) {
        log.info(message);
    }

    private void logE(Throwable e) {
        log.error(e.getMessage());
    }

    private boolean isTaskInProgress(String subtaskResult) {
        return Worker.TASK_IN_PROGRESS.equals(subtaskResult);
    }
}
