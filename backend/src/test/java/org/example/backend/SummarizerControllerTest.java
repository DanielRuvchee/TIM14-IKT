package org.example.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
                "\n" +
                "Every day, while the others huddled to keep warm or dove for food, Gilbert sketched out blueprints in the snow using twigs. He practiced latte art in icy puddles. He even tried to convince a passing seal to invest in his startup, but the seal just sneezed and fell asleep.\n" +
                "\n" +
                "Undeterred, Gilbert began construction. He gathered shiny stones for the counter, used kelp strands as napkins, and trained a grumpy octopus to be the head barista. The octopus was not thrilled but agreed after Gilbert promised to name a drink after him — the Kraken Macchiato.\n" +
                "\n" +
                "After months of slipping, sliding, building, and convincing a couple of shy jellyfish to be mood lighting, the cafe was ready. Gilbert opened the doors — or rather, flapped a seaweed curtain aside — and waited.\n" +
                "\n" +
                "At first, nothing happened.\n" +
                "\n" +
                "Then a curious cod swam in. Then two squid. Then a whole school of clownfish showed up asking if there were gluten-free options.\n" +
                "\n" +
                "Soon, the place was buzzing. Well, bubbling.\n" +
                "\n" +
                "Gilbert stood proudly behind the stone counter, serving drinks with his flippers, smiling wide, living his dream.\n" +
                "\n" +
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
}