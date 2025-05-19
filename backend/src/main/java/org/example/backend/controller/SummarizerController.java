package org.example.backend.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.example.backend.model.Book;
import org.example.backend.model.BookSummaryDTO;
import org.example.backend.repository.BookRepository;
import org.example.backend.service.BookInfoService;
import org.example.backend.service.HuggingFaceSummarizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
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
            System.out.println("Extracted Title: " + bookInfo.title);
            System.out.println("Extracted Author: " + bookInfo.author);

            // 2. Summarize the PDF
            String summary = summarizer.summarizePdf(file);
            if (summary == null || summary.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new BookSummaryDTO("", "", "No summary could be generated."));
            }

            // 3. Create a PDF with title, author, and summary
            String outputPath = "summary_output.pdf";
            PdfWriter writer = new PdfWriter(new FileOutputStream(outputPath));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Add title and author
            document.add(new Paragraph("Title: " + bookInfo.title).setBold());
            document.add(new Paragraph("Author: " + bookInfo.author).setBold());
            document.add(new Paragraph("\n")); // Add spacing
            document.add(new Paragraph("Summary:"));
            document.add(new Paragraph(summary));

            document.close();

            // 4. Return the summary in response
            Book saved = new Book();
            saved.setTitle(bookInfo.title);
            saved.setAuthor(bookInfo.author);
            saved.setContent(summary);
            BookRepository.save(saved);

            BookSummaryDTO dto = new BookSummaryDTO(bookInfo.title, bookInfo.author, summary);

            return ResponseEntity.ok(dto);


        } catch (IOException | InterruptedException e) {
            return ResponseEntity.badRequest()
                    .body(new BookSummaryDTO("", "", "Error processing PDF"));
        }
    }
}