package org.redblaq.wordnet.webapp;

import org.redblaq.wordnet.domain.ProcessorService;

/**
 * Service Injection anti-pattern instead
 */
public enum ServiceProvider {
    INSTANCE;

    private final ProcessorService processorService = new ProcessorService();

    public ProcessorService obtainProcessorService() {
        return processorService;
    }
}
