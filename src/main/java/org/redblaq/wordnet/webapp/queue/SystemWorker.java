package org.redblaq.wordnet.webapp.queue;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import org.redblaq.wordnet.domain.InputUtil;
import org.redblaq.wordnet.domain.services.KnownWordsStore;
import org.redblaq.wordnet.domain.services.OfyService;
import org.redblaq.wordnet.webapp.endpoints.OneTimeOperationsDispatcher;
import org.redblaq.wordnet.domain.entities.Word;
import org.redblaq.wordnet.webapp.services.CompositionRoot;
import org.redblaq.wordnet.webapp.util.Arguments;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * System worker, which si used to perform one-time system operations.
 */
public class SystemWorker extends HttpServlet {

    public static final String URL = "/worker/system";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String operation = req.getParameter(Arguments.OPERATION.toString());

        operations.get(operation).apply(req);
    }

    private static final ImmutableMap<String, Function<HttpServletRequest, Void>> operations =
            new ImmutableMap.Builder<String, Function<HttpServletRequest, Void>>()
                    .put(StoreBaseWordsChunk.class.getSimpleName(), new StoreBaseWordsChunk())
                    .build();

    /**
     * Stores a chunk of base words into datastore.
     */
    public static class StoreBaseWordsChunk implements Function<HttpServletRequest, Void> {

        @Override
        public Void apply(HttpServletRequest request) {
            final String input = request.getParameter(Arguments.ARGUMENT.toString());
            final String[] wordsRaw = input.split(InputUtil.WORDS_SEPARATOR);

            for (String wordRaw : wordsRaw) {
                final Word word = new Word(wordRaw);
                CompositionRoot.INSTANCE.resolve(KnownWordsStore.class).saveWord(word);
            }

            return null;
        }
    }
}
