package org.redblaq.wordnet.domain.services;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import org.redblaq.wordnet.domain.entities.Word;

public class OfyService {

    static {
        ObjectifyService.factory().register(Word.class);
    }

    public static Objectify objectify() {
        return ObjectifyService.ofy();
    }
}
