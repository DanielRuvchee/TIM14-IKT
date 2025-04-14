package org.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.backend.service.HuggingFaceSummarizer;

@RestController
@RequestMapping("/api/summarize")
public class SummarizerController {

    @Autowired
    private HuggingFaceSummarizer summarizer;

    @PostMapping
    public String summarize(@RequestBody String text) {
        return summarizer.summarize(text);
    }
} 