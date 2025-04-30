package org.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.example.backend.service.HuggingFaceSummarizer;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class SummarizerController {

    @Autowired
    private HuggingFaceSummarizer summarizer;

    // Existing endpoint for text input
//    @PostMapping
//    public String summarize(@RequestBody String text) {
//        return summarizer.summarize(text);
//    }

    @PostMapping("/summarize")
    public String summarize(@RequestBody String text) {
        if (text == null || text.trim().isEmpty() || text.equals("\"\"")) {
            return "No summary found: Input text is empty";
        }
        return summarizer.summarize(text.replaceAll("^\"|\"$", ""));
    }

    // New endpoint for PDF upload
//    @PostMapping("/pdf")
//    public String summarizePdf(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return "Error: No file uploaded";
//        }
//        try {
//            // Extract text from PDF
//            String text = summarizer.extractTextFromPdf(file);
//            if (text == null || text.trim().isEmpty()) {
//                return "Error: No text extracted from PDF";
//            }
//            // Summarize extracted text
//            return summarizer.summarize(text);
//        } catch (Exception e) {
//            return "Error processing PDF: " + e.getMessage();
//        }
//    }

/// V1
//    @PostMapping("/summarize/pdf")
//    public String summarizePdf(@RequestParam("file") MultipartFile file) throws IOException {
//        String text = summarizer.extractTextFromPdf(file);
//        if (text == null || text.trim().isEmpty()) {
//            return "No summary found: Input text is empty";
//        }
//        return summarizer.summarize(text);
//    }

/// V2
    @PostMapping("/summarize/pdf")
    public ResponseEntity<String> summarizePdf(@RequestParam("file") MultipartFile file) {
        try {
            String summary = summarizer.summarizePdf(file);
            return ResponseEntity.ok(summary);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing PDF: " + e.getMessage());
        }
    }
} 