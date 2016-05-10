package org.redblaq.wordnet.domain.entities.dto;

public class TaskResponseDto {

    private String taskId;
    private ResponseDto response;

    private TaskResponseDto() {
    }

    public TaskResponseDto(String taskId, ResponseDto response) {
        this.taskId = taskId;
        this.response = response;
    }

    public String getTaskId() {
        return taskId;
    }

    public ResponseDto getResponse() {
        return response;
    }
}
