package org.example;

import java.util.List;

public class DataWrapper {
    private List<BookDTO> content;

    public DataWrapper() {}

    public List<BookDTO> getContent() { return content; }
    public void setContent(List<BookDTO> content) { this.content = content; }
}
