package com.salogics.pdfanalyser.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RAGServiceTest {

    @Autowired
    private RAGService ragService;

    @Test
    @Disabled
    void askAI() {
        var detail = ragService.askAI("I am from Bangalore.");
        System.out.println(detail);
    }

    @Test
    @Disabled
    void ingestDataToVectorStoreTest() {
        ragService.ingestPdfToVectorStore();
    }

    @Test
    @Disabled
    public void testAskAIWithAdvisors(){
        String res = ragService.askAIWithAdvisors("What's your name", "sid123");
        System.out.println(res);
    }

}