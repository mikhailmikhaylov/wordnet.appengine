package org.redblaq.wordnet.domain.services;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import org.redblaq.wordnet.domain.entities.Word;

/**
 * Main Objectify service.
 * <p>Use this service for all Objectify interactions to make sure that entity
 * classes are registered at the factory before we start using them.
 */
public class OfyService {

    static {
        ObjectifyService.factory().register(Word.class);
    }

    @SuppressWarnings("WeakerAccess") // API
    public static Objectify objectify() {
        return ObjectifyService.ofy();
    }
}
