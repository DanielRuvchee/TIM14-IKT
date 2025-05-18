package controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import service.BookInfoService;
import service.BookInfoService.BookInfo;
import service.HuggingFaceSummarizer;
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
    public ResponseEntity<String> summarizePdf(@RequestParam("file") MultipartFile file) {
        try {
            // 1. Extract title and author
            BookInfo bookInfo = bookInfoService.extractTitleAndAuthorFromPdf(file);
            System.out.println("Extracted Title: " + bookInfo.title);
            System.out.println("Extracted Author: " + bookInfo.author);

            // 2. Summarize the PDF
            String summary = summarizer.summarizePdf(file);
            if (summary == null || summary.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("No summary generated from the PDF");
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
            return ResponseEntity.ok(summary);

        } catch (IOException | InterruptedException e) {
            return ResponseEntity.internalServerError()
                    .body("Error processing PDF: " + e.getMessage());
        }
    }
}