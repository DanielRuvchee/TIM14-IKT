package org.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.example.backend.service.HuggingFaceSummarizer;

@RestController
@RequestMapping("/api/summarize")
public class SummarizerController {

    @Autowired
    private HuggingFaceSummarizer summarizer;

    // Existing endpoint for text input
    @PostMapping
    public String summarize(@RequestBody String text) {
        return summarizer.summarize(text);
    }

    // New endpoint for PDF upload
    @PostMapping("/pdf")
    public String summarizePdf(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "Error: No file uploaded";
        }
        try {
            // Extract text from PDF
            String text = summarizer.extractTextFromPdf(file);
            if (text == null || text.trim().isEmpty()) {
                return "Error: No text extracted from PDF";
            }
            // Summarize extracted text
            return summarizer.summarize(text);
        } catch (Exception e) {
            return "Error processing PDF: " + e.getMessage();
        }
    }
} 