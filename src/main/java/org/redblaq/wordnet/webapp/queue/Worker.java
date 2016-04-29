package org.redblaq.wordnet.webapp.queue;

import org.codehaus.jackson.map.ObjectMapper;
import org.redblaq.wordnet.domain.entities.TextEntry;
import org.redblaq.wordnet.domain.entities.dto.ResponseDto;
import org.redblaq.wordnet.webapp.endpoints.Enqueue;
import org.redblaq.wordnet.webapp.services.CacheService;
import org.redblaq.wordnet.webapp.services.ServiceProvider;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class Worker extends HttpServlet {
    private static final String TASK_IN_PROGRESS = "---IN-PROGRESS---";
    private final CacheService cacheService = ServiceProvider.INSTANCE.obtain(CacheService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String taskId = req.getParameter(Enqueue.TASK_ID);
        final String argument = req.getParameter(Enqueue.ARGUMENT);

        cacheService.store(taskId, TASK_IN_PROGRESS);

        final List<TextEntry> textEntries = ServiceProvider.INSTANCE.obtainProcessorService().processText(argument);

        final ResponseDto result = ResponseDto.of(textEntries);

        final ObjectMapper mapper = new ObjectMapper();
        final String jsonResult = mapper.writeValueAsString(result);

        cacheService.store(taskId, jsonResult);
    }
}
