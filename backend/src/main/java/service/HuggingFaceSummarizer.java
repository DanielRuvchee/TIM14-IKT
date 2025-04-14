//package org.example.backend.service;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Collections;
//
//@Service
//public class HuggingFaceSummarizer {
//
//    private final String apiUrl;
//    private final String apiKey;
//    private final RestTemplate restTemplate;
//
//    public HuggingFaceSummarizer(
//            @Value("${huggingface.api.url}") String apiUrl,
//            @Value("${huggingface.api.key}") String apiKey,
//            RestTemplate restTemplate) {
//        this.apiUrl = apiUrl;
//        this.apiKey = apiKey;
//        this.restTemplate = restTemplate;
//    }
//
//    public String summarize(String inputText) {
//        // Prepare headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer " + apiKey.replace("Bearer ", ""));
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//        // Prepare JSON payload
//        String jsonPayload = "{\"inputs\":\"" + inputText.replace("\"", "\\\"") + "\",\"parameters\":{\"max_length\":100,\"min_length\":30}}";
//
//        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);
//
//        try {
//            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
//            String body = response.getBody();
//            // Hugging Face returns [{"summary_text":"..."}]
//            if (body != null && body.contains("summary_text")) {
//                int start = body.indexOf("summary_text\":\"") + 14;
//                int end = body.indexOf("\"", start);
//                return body.substring(start, end);
//            }
//            return "No summary found in response: " + body;
//        } catch (Exception e) {
//            return "Error: " + e.getMessage();
//        }
//    }
//}
package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class HuggingFaceSummarizer {

    private final String apiUrl;
    private final String apiKey;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public HuggingFaceSummarizer(
            @Value("${huggingface.api.url}") String apiUrl,
            @Value("${huggingface.api.key}") String apiKey,
            RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
        System.out.println("API URL: " + apiUrl);
    }

    public String summarize(String inputText) {
        if (inputText == null || inputText.trim().isEmpty()) {
            return "No summary found: Input text is empty";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey.replace("Bearer ", ""));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String jsonPayload = "{\"inputs\":\"" + inputText.replace("\"", "\\\"") + "\",\"parameters\":{\"max_length\":100,\"min_length\":30}}";

        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        try {
            System.out.println("Sending request to: " + apiUrl);
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
            if (response == null) {
                System.err.println("Response is null");
                return "Error: Null response from API";
            }
            String body = response.getBody();
            System.out.println("API response: " + body);
            if (body == null || body.isEmpty()) {
                return "No summary found: Empty response";
            }
            JsonNode jsonArray = objectMapper.readTree(body);
            if (!jsonArray.isArray() || jsonArray.size() == 0) {
                return "No summary found: Invalid response format";
            }
            JsonNode summaryNode = jsonArray.get(0).get("summary_text");
            if (summaryNode == null || summaryNode.isNull()) {
                return "No summary found: Missing summary_text";
            }
            String summary = summaryNode.asText();
            System.out.println("Parsed summary: " + summary); // Debug
            if (summary.isEmpty()) {
                return "No summary found: Empty summary returned";
            }
            return summary;
        } catch (Exception e) {
            System.err.println("Summarization error: " + e.getMessage());
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}