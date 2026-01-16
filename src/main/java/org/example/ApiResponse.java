package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {
    private String message;
    private DataWrapper data;

    public ApiResponse() {}

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public DataWrapper getData() { return data; }
    public void setData(DataWrapper data) { this.data = data; }
}
