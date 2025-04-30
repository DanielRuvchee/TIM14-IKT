//package org.example.backend.service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.text.PDFTextStripper;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//@Service
//public class HuggingFaceSummarizer {
//
//    private final String apiUrl;
//    private final String apiKey;
//    private final RestTemplate restTemplate;
//    private final ObjectMapper objectMapper;
//
//    public HuggingFaceSummarizer(
//            @Value("${huggingface.api.url}") String apiUrl,
//            @Value("${huggingface.api.key}") String apiKey,
//            RestTemplate restTemplate) {
//        this.apiUrl = apiUrl;
//        this.apiKey = apiKey;
//        this.restTemplate = restTemplate;
//        this.objectMapper = new ObjectMapper();
//        System.out.println("API URL: " + apiUrl);
//    }
//
//    public String summarize(String inputText) {
//        if (inputText == null || inputText.trim().isEmpty()) {
//            return "No summary found: Input text is empty";
//        }
//
//        // Chunk text if too large (Hugging Face has a token limit, ~512 tokens)
//        List<String> chunks = chunkText(inputText, 30000); // Approx 1000 characters per chunk
//        List<String> summaries = new ArrayList<>();
//
//        for (String chunk : chunks) {
//            String summary = summarizeChunk(chunk);
//            if (!summary.startsWith("Error") && !summary.startsWith("No summary found")) {
//                summaries.add(summary);
//            }
//        }
//
//        if (summaries.isEmpty()) {
//            return "No summary found: Unable to summarize any chunks";
//        }
//
//        // Combine summaries
//        return String.join(" ", summaries);
//    }
//
//    private String summarizeChunk(String chunk) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer " + apiKey.replace("Bearer ", ""));
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//        String jsonPayload = "{\"inputs\":\"" + chunk.replace("\"", "\\\"") + "\",\"parameters\":{\"max_length" +
//                "\":300,\"min_length\":50,\"no_repeat_ngram_size" +
//                "\":3,\"encoder_no_repeat_ngram_size\":3," +
//                "\"repetition_penalty\":3.5,\"num_beams\":4,\"early_stopping\":true}}";
//
//
//        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);
//
//        try {
//            System.out.println("Sending request to: " + apiUrl);
//            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
//            if (response == null) {
//                System.err.println("Response is null");
//                return "Error: Null response from API";
//            }
//            String body = response.getBody();
//            System.out.println("API response: " + body);
//            if (body == null || body.isEmpty()) {
//                return "No summary found: Empty response";
//            }
//            JsonNode jsonArray = objectMapper.readTree(body);
//            if (!jsonArray.isArray() || jsonArray.size() == 0) {
//                return "No summary found: Invalid response format";
//            }
//            JsonNode summaryNode = jsonArray.get(0).get("summary_text");
//            if (summaryNode == null || summaryNode.isNull()) {
//                return "No summary found: Missing summary_text";
//            }
//            String summary = summaryNode.asText();
//            System.out.println("Parsed summary: " + summary);
//            if (summary.isEmpty()) {
//                return "No summary found: Empty summary returned";
//            }
//            return summary;
//
//        } catch (Exception e) {
//
//            System.err.println("Summarization error: " + e.getMessage());
//            e.printStackTrace();
//            return "Error: " + e.getMessage();
//        }
//
//    }
//
//
//    public String extractTextFromPdf(MultipartFile file) throws IOException {
//        try (PDDocument document = PDDocument.load(file.getInputStream())) {
//            if (document.isEncrypted()) {
//                return null; // Cannot process encrypted PDFs
//            }
//            PDFTextStripper stripper = new PDFTextStripper();
//            String text = stripper.getText(document);
//            return text != null ? text.trim() : "";
//        }
//    }
//
//    private List<String> chunkText(String text, int chunkSize) {
//        List<String> chunks = new ArrayList<>();
//        int length = text.length();
//        for (int i = 0; i < length; i += chunkSize) {
//            int end = Math.min(i + chunkSize, length);
//            chunks.add(text.substring(i, end));
//        }
//        return chunks;
//    }
//}

//V2
//package org.example.backend.service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.text.PDFTextStripper;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.HttpServerErrorException;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.*;
//
//@Service
//public class HuggingFaceSummarizer {
//
//    private final String apiUrl;
//    private final RestTemplate restTemplate;
//    private final ObjectMapper objectMapper;
//
//    public HuggingFaceSummarizer(
//            @Value("${flask.api.url}") String apiUrl,
//            RestTemplate restTemplate) {
//        this.apiUrl = apiUrl;
//        this.restTemplate = restTemplate;
//        this.objectMapper = new ObjectMapper();
//        System.out.println("Flask API URL: " + apiUrl);
//    }
//
//    public String summarize(String inputText) {
//        if (inputText == null || inputText.trim().isEmpty()) {
//            return "No summary found: Input text is empty";
//        }
//
//        // Chunk text to align with Flask API's token limit (14,000 tokens, ~50,000 characters)
//        List<String> chunks = chunkText(inputText, 50000);
//        List<String> summaries = new ArrayList<>();
//
//        for (String chunk : chunks) {
//            String summary = summarizeChunk(chunk);
//            if (!summary.startsWith("Error") && !summary.startsWith("No summary found")) {
//                summaries.add(summary);
//            }
//        }
//
//        if (summaries.isEmpty()) {
//            return "No summary found: Unable to summarize any chunks";
//        }
//
//        // Combine summaries
//        return String.join(" ", summaries);
//    }
//
//    private String summarizeChunk(String chunk) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//        // Create payload using a Map to ensure proper JSON serialization
//        Map<String, String> payload = new HashMap<>();
//        payload.put("text", chunk);
//
//        try {
//            // Convert payload to JSON string
//            String jsonPayload = objectMapper.writeValueAsString(payload);
//            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);
//
//            System.out.println("Sending request to: " + apiUrl + " with payload: " + jsonPayload);
//            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
//
//            if (response == null || response.getBody() == null) {
//                System.err.println("Response is null or empty");
//                return "Error: Null response from API";
//            }
//
//            String body = response.getBody();
//            System.out.println("API response: " + body);
//
//            // Parse Flask API response
//            JsonNode jsonNode = objectMapper.readTree(body);
//            if (jsonNode.has("error")) {
//                return "Error: " + jsonNode.get("error").asText();
//            }
//            if (!jsonNode.has("summary")) {
//                return "No summary found: Invalid response format";
//            }
//
//            String summary = jsonNode.get("summary").asText();
//            if (summary.isEmpty()) {
//                return "No summary found: Empty summary returned";
//            }
//            return summary;
//
//        } catch (HttpClientErrorException | HttpServerErrorException e) {
//            System.err.println("Summarization error: " + e.getMessage());
//            e.printStackTrace();
//            return "Error: " + e.getResponseBodyAsString();
//        } catch (Exception e) {
//            System.err.println("Summarization error: " + e.getMessage());
//            e.printStackTrace();
//            return "Error: " + e.getMessage();
//        }
//    }
//
//    public String extractTextFromPdf(MultipartFile file) throws IOException {
//        try (PDDocument document = PDDocument.load(file.getInputStream())) {
//            if (document.isEncrypted()) {
//                return null; // Cannot process encrypted PDFs
//            }
//            PDFTextStripper stripper = new PDFTextStripper();
//            String text = stripper.getText(document);
//            return text != null ? text.trim() : "";
//        }
//    }
//
//    private List<String> chunkText(String text, int chunkSize) {
//        List<String> chunks = new ArrayList<>();
//        int length = text.length();
//        for (int i = 0; i < length; i += chunkSize) {
//            int end = Math.min(i + chunkSize, length);
//            chunks.add(text.substring(i, end));
//        }
//        return chunks;
//    }
//
//
//    public String summarizePdf(MultipartFile file) throws IOException {
//        if (file == null || file.isEmpty()) {
//            throw new IllegalArgumentException("No file provided or file is empty");
//        }
//
//        // Prepare multipart request
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//        body.add("file", new FileSystemResource(convertMultipartFileToFile(file)));
//
//        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//
//        // Send request to Flask API
//        try {
//            ResponseEntity<String> response = restTemplate.postForEntity(
//                    apiUrl + "/summarize",
//                    requestEntity,
//                    String.class
//            );
//
//            if (response.getStatusCode() == HttpStatus.OK) {
//                String responseBody = response.getBody();
//                // Simple JSON parsing (use Jackson for production)
//                if (responseBody.contains("\"error\"")) {
//                    throw new IOException("Flask API error: " + responseBody);
//                }
//                String summary = responseBody.substring(responseBody.indexOf("\"summary\":\"") + 11, responseBody.lastIndexOf("\"}"));
//                return summary.replace("\\n", " ");
//            } else {
//                throw new IOException("Flask API returned status: " + response.getStatusCode());
//            }
//        } catch (Exception e) {
//            throw new IOException("Error communicating with Flask API: " + e.getMessage(), e);
//        }
//    }
//
//    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
//        File tempFile = File.createTempFile("pdf_", ".pdf");
//        file.transferTo(tempFile);
//        tempFile.deleteOnExit();
//        return tempFile;
//    }
//}

//V3

package org.example.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class HuggingFaceSummarizer {

    private final RestTemplate restTemplate;
    private final String flaskApiUrl;
    private final ObjectMapper objectMapper;

    public HuggingFaceSummarizer(RestTemplate restTemplate, @Value("${flask.api.url:http://localhost:5000}") String flaskApiUrl) {
        this.restTemplate = restTemplate;
        this.flaskApiUrl = flaskApiUrl;
        this.objectMapper = new ObjectMapper();
    }

    public String summarizePdf(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("No file provided or file is empty");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(convertMultipartFileToFile(file)));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            String endpoint = flaskApiUrl.endsWith("/") ? flaskApiUrl + "summarize" : flaskApiUrl + "/summarize";
            System.out.println("Sending request to Flask API: " + endpoint);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    endpoint,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new IOException("Flask API returned status: " + response.getStatusCode() + ", body: " + response.getBody());
            }

            String responseBody = response.getBody();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            if (jsonNode.has("error")) {
                throw new IOException("Flask API error: " + jsonNode.get("error").asText());
            }
            if (!jsonNode.has("summary")) {
                throw new IOException("Flask API response missing 'summary' field: " + responseBody);
            }

            String summary = jsonNode.get("summary").asText();
            if (summary.isEmpty() || summary.contains("No summary found")) {
                return "No valid summary generated by Flask API";
            }
            return summary.replace("\\n", " ");
        } catch (Exception e) {
            throw new IOException("Error communicating with Flask API: " + e.getMessage(), e);
        }
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("pdf_", ".pdf");
        file.transferTo(tempFile);
        tempFile.deleteOnExit();
        return tempFile;
    }

    public String summarize(String inputText) {
        if (inputText == null || inputText.trim().isEmpty()) {
            return "No summary found: Input text is empty";
        }
        return "Existing text summarization logic";
    }
}