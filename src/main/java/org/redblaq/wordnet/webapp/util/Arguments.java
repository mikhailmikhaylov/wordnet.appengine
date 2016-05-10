package org.redblaq.wordnet.webapp.util;

/**
 * Common arguments for inter-servlet communication.
 * <p>Arguments do not define interactions contract.
 * <p>Naming contract: CALL_OPERATION -> call-operation
 */
public enum Arguments {

    OPERATION,
    ARGUMENT,
    TASK_ID,
    CHUNK_TASK_ID,
    CHUNK_OFFSET,
    CHANNEL_ID;

    @Override
    public String toString() {
        return super.toString().toLowerCase().replace("_", "-");
    }
}
