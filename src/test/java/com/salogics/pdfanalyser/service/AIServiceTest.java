package com.salogics.pdfanalyser.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AIServiceTest {

    @Autowired
    private AIService aiService;

    @Test
    void getJoke() {
        var detail = aiService.getDetails("iron man");
        System.out.println(detail);
    }

    @Test
    void ingestDataToVectorStoreTest() {
        aiService.ingestDataToVectorStore("iron man");
    }

    @Test
    void similaritySearchTest() {
        var res = aiService.similaritySearch("iron");
        System.out.println(res);
    }

    @Test
    void testEmbeddingMsg() {
        var embedding = aiService.getEmbedding("iron man");
        System.out.println(embedding.length);

        for(float e : embedding){
            System.out.print(e);
        }
    }
}