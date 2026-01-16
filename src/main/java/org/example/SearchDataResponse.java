package org.example;

import java.util.ArrayList;
import java.util.List;

public class SearchDataResponse {
    private String message;
    private List<BookDTO> data = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public List<BookDTO> getData() {
        return data;
    }
}
