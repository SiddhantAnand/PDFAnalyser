# PDF Analyser

A Spring Boot application that uses **RAG (Retrieval-Augmented Generation)** to analyze PDF documents. It ingests PDFs into a vector store, then answers questions using context retrieved from the stored documents.

## Tech Stack

- **Spring Boot 4.0.6** with Spring AI 2.0.0-M8
- **Ollama (llama3.2)** — Chat/LLM (fully local, no API keys needed)
- **Ollama (nomic-embed-text)** — Local embeddings
- **PgVector (PostgreSQL)** — Vector store
- **JDBC Chat Memory** — Short-term conversation memory
- **Lombok**

## Prerequisites

- Java 21
- [Ollama](https://ollama.com/) installed and running
- PostgreSQL with pgvector extension (or use Docker)

## Setup

### 1. Start PostgreSQL with pgvector

```bash
docker run -d \
  --name pgvector \
  -p 5434:5432 \
  -e POSTGRES_DB=vector-db \
  -e POSTGRES_USER=your-username \
  -e POSTGRES_PASSWORD=your-password \
  pgvector/pgvector:pg17
```

### 2. Pull the Ollama models

```bash
ollama pull llama3.2
ollama pull nomic-embed-text
```

### 3. Configure the application

Copy the example config and fill in your credentials:

```bash
cp src/main/resources/application-example.yaml src/main/resources/application.yaml
```

Update `application.yaml` with:
- Your PostgreSQL credentials

### 4. Build and run

```bash
./mvnw spring-boot:run
```

### 5. Run tests

```bash
./mvnw test
```

## Project Structure

```
src/main/java/com/salogics/pdfanalyser/
├── PdfanalyserApplication.java       # Main application
├── config/
│   └── AIConfig.java                 # ChatClient bean configuration
└── service/
    ├── AIService.java                # Chat & embedding service
    └── RAGService.java               # PDF ingestion & RAG query service
```

## How It Works

1. **Ingest**: `RAGService.ingestPdfToVectorStore()` reads a PDF, splits it into chunks, and stores embeddings in PgVector using Ollama (local).
2. **Query**: `RAGService.askAI(prompt)` performs a similarity search on the vector store, retrieves relevant chunks, and sends them as context to Ollama (llama3.2) for a grounded response.
3. **Chat with Memory**: `RAGService.askAIWithAdvisors(prompt, userId)` uses both short-term memory (MessageChatMemoryAdvisor via JDBC) and long-term memory (VectorStoreChatMemoryAdvisor) to maintain conversation context across turns.
