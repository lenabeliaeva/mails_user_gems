package org.example.mails.events.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.example.mails.config.ApiKeys;
import org.example.mails.config.ExternalApiUrls;
import org.example.mails.events.dto.EventApiResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
@AllArgsConstructor
public class EventApiClient {
    private final ExternalApiUrls externalApiUrls;
    private final ObjectMapper objectMapper;

    public List<EventApiResponse> fetchNewEventsFromApi(String email, int pageNum) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String url = externalApiUrls.getCalendarBaseUrl();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "?page=" + pageNum))
                .header("Authorization", "Bearer " + ApiKeys.getToken(email))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), new TypeReference<>() {});
    }
}
