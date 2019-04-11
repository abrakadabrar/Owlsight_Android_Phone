package com.cryptocenter.andrey.owlsight.data.model.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ErrorBody {
    @SerializedName("errors")
    private List<String> errors;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private boolean result;
    @SerializedName("success")
    private boolean success;

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
