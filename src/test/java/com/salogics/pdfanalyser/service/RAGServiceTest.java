package com.salogics.pdfanalyser.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RAGServiceTest {

    @Autowired
    private RAGService ragService;

    @Test
    void askAI() {
        var detail = ragService.askAI("What is different types of design pattern?");
        System.out.println(detail);
    }

    @Test
    void ingestDataToVectorStoreTest() {
        ragService.ingestPdfToVectorStore();
    }

}