package org.redblaq.wordnet.webapp.util;

public enum Responses {

    UNKNOWN_OPERATION {
        @Override
        public String getText() {
            return "---Unknown-operation---";
        }
    },
    CHUNKS_ENQUEUED {
        @Override
        public String getText() {
            return "---Chunks-enqueued---";
        }
    },
    TASK_NOT_FOUND {
        @Override
        public String getText() {
            return "---Task-not-found---";
        }
    },
    ERROR {
        @Override
        public String getText() {
            return "---ERROR---";
        }
    },
    IN_PROGRESS {
        @Override
        public String getText() {
            return "---IN-PROGRESS---";
        }
    };


    public abstract String getText();
}
