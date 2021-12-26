package com.project.azul.dto;

public class CheckDto {
    private boolean succeed;
    private String message;

    public CheckDto(boolean succeed, String message) {
        this.succeed = succeed;
        this.message = message;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public String getMessage() {
        return message;
    }
}
