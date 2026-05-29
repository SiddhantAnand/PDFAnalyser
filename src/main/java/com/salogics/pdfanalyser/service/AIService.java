package com.salogics.pdfanalyser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIService {

    private final ChatClient chatClient;
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;

    public void ingestDataToVectorStore(String text){
        Document document = new Document(text);
        vectorStore.add(List.of(document));
    }

    public List<Document> similaritySearch(String text){
        return vectorStore.similaritySearch(text);
    }

    public float[] getEmbedding(String text){
        return embeddingModel.embed(text);
    }

    public String getDetails(String topic){
        String systemPrompt = """
                You are a good communicator, and you can converse in 2 lines.
                You give crisp and at the same time a detailed out in 2 lines itself.
                Give me details on the topic : {topic}
                """;
        PromptTemplate promptTemplate = new PromptTemplate(systemPrompt);
        String text = promptTemplate.render(Map.of("topic", topic));
        var response = chatClient.prompt()
                .user(text)
                .advisors(
                        new SimpleLoggerAdvisor()
                )
                .call()
                .chatClientResponse();

        return response.chatResponse().getResult().getOutput().getText();
    }
}
