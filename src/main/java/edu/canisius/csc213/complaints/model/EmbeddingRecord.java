package edu.canisius.csc213.complaints.model;

import java.util.List;

public class EmbeddingRecord {
    private String id;
    private List<Double> embedding;

    public String getId() {
        return id;
    }

    public void setId(String Id, String id) {
        this.id = id;
    }

    public List<Double> getEmbedding() {
        return embedding;
    }

    public void setEmbedding(List<Double> embedding) {
        this.embedding = embedding;
    }
}