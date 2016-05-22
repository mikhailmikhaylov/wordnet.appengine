package org.redblaq.wordnet.domain.entities.dto;

public class TaskResponseDto {

    private String taskId;
    private ResponseDto response;

    @SuppressWarnings("unused") // Explicitly defined for Jackson
    private TaskResponseDto() {
    }

    public TaskResponseDto(String taskId, ResponseDto response) {
        this.taskId = taskId;
        this.response = response;
    }

    @SuppressWarnings("unused") // Getter
    public String getTaskId() {
        return taskId;
    }

    public ResponseDto getResponse() {
        return response;
    }
}
