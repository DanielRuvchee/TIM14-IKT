package org.example.backend;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SummarizerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSummarizeEndpoint() throws Exception {
        String requestBody = "\"In the frozen stretches of the south pole, there lived a penguin named Gilbert. Unlike the others in his colony, Gilbert was not content waddling around, sliding on his belly, and fishing in icy waters all day. Gilbert had dreams. Big ones. He wanted to open the first-ever underwater cafe for fish — not for eating them, but for serving them cappuccinos made of plankton froth and seaweed biscuits.\n" +
                "Every day, while the others huddled to keep warm or dove for food, Gilbert sketched out blueprints in the snow using twigs. He practiced latte art in icy puddles. He even tried to convince a passing seal to invest in his startup, but the seal just sneezed and fell asleep.\n" +
                "Undeterred, Gilbert began construction. He gathered shiny stones for the counter, used kelp strands as napkins, and trained a grumpy octopus to be the head barista. The octopus was not thrilled but agreed after Gilbert promised to name a drink after him — the Kraken Macchiato.\n" +
                "After months of slipping, sliding, building, and convincing a couple of shy jellyfish to be mood lighting, the cafe was ready. Gilbert opened the doors — or rather, flapped a seaweed curtain aside — and waited.\n" +
                "At first, nothing happened.\n" +
                "Then a curious cod swam in. Then two squid. Then a whole school of clownfish showed up asking if there were gluten-free options.\n" +
                "Soon, the place was buzzing. Well, bubbling.\n" +
                "Gilbert stood proudly behind the stone counter, serving drinks with his flippers, smiling wide, living his dream.\n" +
                "To this day, deep beneath the ice, if you listen closely, you might hear the faint sounds of aquatic jazz and the soft clink of sea shells in tiny coffee cups.\"";

        mockMvc.perform(post("/api/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println("Response: " + result.getResponse().getContentAsString()));
    }

    @Test
    public void testSummarizeEmptyInput() throws Exception {
        String requestBody = "\"\"";

        mockMvc.perform(post("/api/summarize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println("Response: " + result.getResponse().getContentAsString()));
    }

    @Test
    public void testSummarizePdfEndpoint() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("In the frozen stretches of the south pole, there lived a penguin named Gilbert...");
            contentStream.endText();
            contentStream.close();
            document.save(baos);
        }

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                baos.toByteArray()
        );

        mockMvc.perform(multipart("/api/summarize/pdf")
                        .file(file))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println("Response: " + result.getResponse().getContentAsString()));
    }

    @Test
    public void testSummarizeEmptyPdf() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PDDocument document = new PDDocument()) {
            document.addPage(new PDPage());
            document.save(baos);
        }

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty.pdf",
                "application/pdf",
                baos.toByteArray()
        );

        mockMvc.perform(multipart("/api/summarize/pdf")
                        .file(file))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println("Response: " + result.getResponse().getContentAsString()));
    }

    @Test
    public void testSummarizeBookPdfFromExternalPath() throws Exception {
        // Specify the path to your book PDF on your computer
        String pdfPath = "C:\\Users\\Nikola\\Downloads\\Meditations.pdf"; // Replace with actual path, e.g., "/home/user/documents/book.pdf"
        Path path = Paths.get(pdfPath);

        // Check if the file exists
        if (!Files.exists(path)) {
            throw new IllegalStateException("Book PDF not found at: " + pdfPath);
        }

        // Read the PDF file content
        byte[] pdfContent = Files.readAllBytes(path);

        // Create a MockMultipartFile to simulate the file upload
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "book.pdf",
                "application/pdf",
                pdfContent
        );

        // Perform the request and verify
        mockMvc.perform(multipart("/api/summarize/pdf")
                        .file(file))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String response = result.getResponse().getContentAsString();
                    System.out.println("Book PDF Summary: " + response);
                });
    }
}