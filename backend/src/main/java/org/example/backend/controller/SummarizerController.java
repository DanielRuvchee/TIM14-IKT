package org.example.backend.controller;
import org.example.backend.model.Book;
import org.example.backend.model.BookSummaryDTO;
import org.example.backend.repository.BookRepository;
import org.example.backend.service.BookInfoService;
import org.example.backend.service.HuggingFaceSummarizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class SummarizerController {
    @Autowired
    private BookRepository BookRepository;

    @Autowired
    private HuggingFaceSummarizer summarizer;

    @Autowired
    private BookInfoService bookInfoService;

    @PostMapping("/summarize")
    public String summarize(@RequestBody String text) {
        if (text == null || text.trim().isEmpty() || text.equals("\"\"")) {
            return "No summary found: Input text is empty";
        }
        return summarizer.summarize(text.replaceAll("^\"|\"$", ""));
    }

    @PostMapping("/summarize/pdf")
    public ResponseEntity<BookSummaryDTO> summarizePdf(@RequestParam("file") MultipartFile file) {
        try {

            BookInfoService.BookInfo bookInfo = bookInfoService.extractTitleAndAuthorFromPdf(file);

            String summary = summarizer.summarizePdf(file);
            if (summary == null || summary.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new BookSummaryDTO("", "", "No summary could be generated."));
            }
            String title = bookInfo.title;
            String author = bookInfo.author;
            String category = bookInfo.category;

            Book saved = new Book();
            saved.setTitle(title);
            saved.setAuthor(author);
            saved.setContent("summary");
            saved.setCategory(category);
            BookRepository.save(saved);

            BookSummaryDTO dto = new BookSummaryDTO(bookInfo.title, bookInfo.author, "summary");

            return ResponseEntity.ok(dto);


        } catch (IOException | InterruptedException e) {
            return ResponseEntity.badRequest()
                    .body(new BookSummaryDTO("", "", "Error processing PDF"));
        }
    }
}