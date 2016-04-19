package org.redblaq.wordnet.webapp;

import org.redblaq.wordnet.domain.ProcessorService;

// Because I don't care
/* package */ enum ServiceProvider {
    INSTANCE;

    private final ProcessorService processorService = new ProcessorService();

    public ProcessorService obtainProcessorService() {
        return processorService;
    }
}
