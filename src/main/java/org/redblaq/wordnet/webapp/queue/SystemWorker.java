package org.redblaq.wordnet.webapp.queue;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import org.redblaq.wordnet.domain.InputRules;
import org.redblaq.wordnet.domain.services.OfyService;
import org.redblaq.wordnet.webapp.endpoints.OneTimeOperationsDispatcher;
import org.redblaq.wordnet.domain.BaseWordsStore;
import org.redblaq.wordnet.domain.entities.Word;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SystemWorker extends HttpServlet {

    public static final String VALUE_ARG = "value";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String operation = req.getParameter(OneTimeOperationsDispatcher.OPERATION_ARG);

        operations.get(operation).apply(req);
    }

    private static final ImmutableMap<String, Function<HttpServletRequest, Void>> operations =
            new ImmutableMap.Builder<String, Function<HttpServletRequest, Void>>()
                    .put(StoreBaseWordsChunk.class.getSimpleName(), new StoreBaseWordsChunk())
                    .build();

    public static class StoreBaseWordsChunk implements Function<HttpServletRequest, Void> {

        @Override
        public Void apply(HttpServletRequest request) {
            final String input = request.getParameter(VALUE_ARG);
            final String[] wordsRaw = input.split(InputRules.WORDS_SEPARATOR);

            for (String wordRaw : wordsRaw) {
                final Word word = new Word(wordRaw);
                // No need to build forms, I suppose. Needs result comparison
                // final Word word = ServiceProvider.obtainService(WordService.class).buildWord(wordRaw);
                OfyService.objectify().save().entity(word).now();
            }

            return null;
        }
    }
}
