package org.example.service;

import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    @Autowired
    private RestTemplate restTemplate;

    private String sessionId;

    private static final String BASE_URL = "http://94.198.50.185:7081/api/users";

    public String executeOperations() {
        // Step 1: Get all users and save session ID
        ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL, String.class);
        HttpHeaders headers = response.getHeaders();
        sessionId = headers.getFirst(HttpHeaders.SET_COOKIE);

        // Step 2: Add a new user
        User newUser = new User(3L, "James", "Brown", (byte) 25);
        HttpHeaders postHeaders = new HttpHeaders();
        postHeaders.setContentType(MediaType.APPLICATION_JSON);
        postHeaders.set(HttpHeaders.COOKIE, sessionId);
        HttpEntity<User> postRequest = new HttpEntity<>(newUser, postHeaders);
        ResponseEntity<String> postResponse = restTemplate.postForEntity(BASE_URL, postRequest, String.class);
        String part1 = postResponse.getBody();

        // Step 3: Update the user
        User updatedUser = new User(3L, "Thomas", "Shelby", (byte) 25);
        HttpHeaders putHeaders = new HttpHeaders();
        putHeaders.setContentType(MediaType.APPLICATION_JSON);
        putHeaders.set(HttpHeaders.COOKIE, sessionId);
        HttpEntity<User> putRequest = new HttpEntity<>(updatedUser, putHeaders);
        ResponseEntity<String> putResponse = restTemplate.exchange(BASE_URL, HttpMethod.PUT, putRequest, String.class);
        String part2 = putResponse.getBody();

        // Step 4: Delete the user
        HttpHeaders deleteHeaders = new HttpHeaders();
        deleteHeaders.set(HttpHeaders.COOKIE, sessionId);
        HttpEntity<String> deleteRequest = new HttpEntity<>(deleteHeaders);
        ResponseEntity<String> deleteResponse = restTemplate.exchange(BASE_URL + "/3", HttpMethod.DELETE, deleteRequest, String.class);
        String part3 = deleteResponse.getBody();

        // Concatenate the parts of the code
        return part1 + part2 + part3;
    }
}