package org.example.mails.person.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.example.mails.config.ApiKeys;
import org.example.mails.config.ExternalApiUrls;
import org.example.mails.person.dto.PersonDto;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@AllArgsConstructor
public class PersonApiClient {
    private final ExternalApiUrls externalApiUrls;
    private final ObjectMapper objectMapper;

    public PersonDto fetchNewPerson(String email) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String url = externalApiUrls.getPersonBaseUrl();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + email))
                .header("Authorization", "Bearer " + ApiKeys.getToken(email))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), PersonDto.class);
    }
}
