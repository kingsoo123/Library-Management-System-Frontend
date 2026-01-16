package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;



public class BookApiService {
    private static final String API_URL = "http://localhost:9090/api/books";
    HttpClient client = HttpClient.newHttpClient();


    public List<Book> fetchBooks(int page, int size) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "?page=" + page + "&size=" + size ))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ApiResponse apiResponse = mapper.readValue(response.body(), ApiResponse.class);
        List<BookDTO> dtos = apiResponse.getData() != null
                ? apiResponse.getData().getContent()
                : List.of(); // safe empty list if null

        return dtos.stream()
                .map(dto -> new Book((long) dto.getId(), dto.getTitle(), dto.getAuthor(), dto.getIsbn(), dto.getPublishedDate()))
                .collect(Collectors.toList());

    }



    public void createBook(Book book) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(book);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.discarding());
    }


    public void updateBook(Book book, int id) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(book);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.discarding());
    }


    public void deleteBook(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.discarding());
    }


    public List<Book> searchBook(String title) throws Exception {
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/search?title=" + encodedTitle))
                .header("Content-Type", "application/json")
                .GET()
                .build();



        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SearchDataResponse apiResponse = mapper.readValue(response.body(), SearchDataResponse.class);


        return apiResponse.getData().stream()
                .map(dto -> new Book(
                        (long) dto.getId(),
                        dto.getTitle(),
                        dto.getAuthor(),
                        dto.getIsbn(),
                        dto.getPublishedDate()
                ))
                .collect(Collectors.toList());



    }


}
