package com.salogics.pdfanalyser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RAGService {

    private final ChatClient chatClient;
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;

    @Value("classpath:Design patterns.pdf")
    Resource pdfFile;

    public String askAI(String prompt){
        String template = """
                Rules:
                - Use Only the information provided in the context
                - You May rephrase, summarize, and explain in natural language
                - Do NOT introduce new concepts or facts
                - If multiple context sections are relevant, combine then into a single explanation.
                - If the answer is not present, say "Sorry! i am not aware of this"
                
                Context:
                {context}
                
                Answer in a friendly, conversational tone.
                """;
        List<Document> documents = vectorStore.similaritySearch(SearchRequest.builder()
                .query(prompt)
                .topK(2)
                .similarityThreshold(0.5)
                .filterExpression("file_name == 'Design patterns.pdf'")
                .build());
        String context = documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));
        PromptTemplate promptTemplate = new PromptTemplate(template);
        String systemPrompt = promptTemplate.render(Map.of("context", context));

        return  chatClient.prompt()
                .system(systemPrompt)
                .user(prompt)
                .advisors(new SimpleLoggerAdvisor())
                .call()
                .content();
    }

    public void ingestPdfToVectorStore(){
        PagePdfDocumentReader reader = new PagePdfDocumentReader(pdfFile);
        List<Document> pages = reader.get();

        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(400)
                .build();
        List<Document> chunks = splitter.apply(pages);
        vectorStore.add(chunks);
    }
}
