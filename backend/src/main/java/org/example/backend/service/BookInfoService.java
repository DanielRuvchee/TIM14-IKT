package org.example.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class BookInfoService {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String apiEndpoint = "https://openrouter.ai/api/v1/chat/completions";

    @Value("${openrouter.api.key:}")
    private String apiKey;

    public static class BookInfo {
        public final String title;
        public final String author;

        public BookInfo(String title, String author) {
            this.title = title;
            this.author = author;
        }
    }

    public BookInfo extractTitleAndAuthorFromPdf(MultipartFile file) throws IOException, InterruptedException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("PDF file is null or empty");
        }

        try (PdfReader reader = new PdfReader(new ByteArrayInputStream(file.getBytes()));
             PdfDocument pdfDoc = new PdfDocument(reader)) {
            StringBuilder text = new StringBuilder();
            int maxPages = Math.min(pdfDoc.getNumberOfPages(), 3);
            for (int i = 1; i <= maxPages; i++) {
                text.append(PdfTextExtractor.getTextFromPage(pdfDoc.getPage(i)));
            }
            String extractedText = text.toString();
            System.out.println("Extracted PDF text (first 1500 chars): " + extractedText.substring(0, Math.min(extractedText.length(), 1500)));

            // Prepare prompt for DeepSeek API
            String prompt = "Extract the title and author of the book from the following text. The title should be the primary book title (e.g., 'Meditations'), not a series, edition, or collection name. Ignore metadata like 'Edited by' or 'Translated by'. Respond with only the title and author, each on a new line:\n\n" + extractedText;

            String jsonPayload = objectMapper.writeValueAsString(Map.of(
                    "model", "deepseek/deepseek-chat:free",
                    "messages", List.of(
                            Map.of("role", "user", "content", prompt)
                    ),
                    "max_tokens", 100,
                    "temperature", 0.1,
                    "response_format", Map.of("type", "text")
            ));

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(apiEndpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload));

            if (!apiKey.isEmpty()) {
                requestBuilder.header("Authorization", "Bearer " + apiKey);
            }

            HttpRequest request = requestBuilder.build();
            System.out.println("Sending DeepSeek API request to: " + apiEndpoint);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("DeepSeek API response: Status " + response.statusCode() + ", Body: " + response.body());

            if (response.statusCode() != 200) {
                System.err.println("DeepSeek API error: Status " + response.statusCode() + ", Body: " + response.body());
                return new BookInfo("Error extracting title", "Error extracting author");
            }

            try {
                String content = objectMapper.readTree(response.body())
                        .get("choices")
                        .get(0)
                        .get("message")
                        .get("content")
                        .asText()
                        .trim();
                System.out.println("Parsed DeepSeek response: " + content);

                String[] lines = content.split("\n");
                String title = lines.length > 0 ? lines[0].trim() : "Unknown Title";
                String author = lines.length > 1 ? lines[1].trim() : "Unknown Author";

                return new BookInfo(title, author);
            } catch (Exception e) {
                System.err.println("Error parsing DeepSeek response: " + e.getMessage());
                return new BookInfo("Error extracting title", "Error extracting author");
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error processing PDF or API request: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return new BookInfo("Error extracting title", "Error extracting author");
        }
    }
}