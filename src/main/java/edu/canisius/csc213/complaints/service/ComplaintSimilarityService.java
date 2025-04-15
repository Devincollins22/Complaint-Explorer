package edu.canisius.csc213.complaints.service;

import edu.canisius.csc213.complaints.model.Complaint;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ComplaintSimilarityService {

    private final List<Complaint> complaints;

    public ComplaintSimilarityService(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    public List<Complaint> findTop3Similar(Complaint target) {
     
        double[] targetEmbedding = target.getEmbedding();
        if (targetEmbedding == null) return List.of();

        List<ComplaintWithScore> scored = new ArrayList<>();

        for (Complaint other : complaints) {
            if (other.getComplaintId() == target.getComplaintId()) continue; 
            double[] otherEmbedding = other.getEmbedding();
            if (otherEmbedding != null) {
                double score = cosineSimilarity(targetEmbedding, otherEmbedding);
                scored.add(new ComplaintWithScore(other, score));
            }
        }
        return scored.stream()
            .sorted(Comparator.comparingDouble(c -> -c.score))
            .limit(3)
            .map(c -> c.complaint)
            .collect(Collectors.toList());
    }

    private double cosineSimilarity(double[] a, double[] b) {
        if (a.length != b.length) return 0.0;

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < a.length; i++) {
            dotProduct += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }

        if (normA == 0 || normB == 0) return 0.0;

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private static class ComplaintWithScore {
        Complaint complaint;
        double score;

        ComplaintWithScore(Complaint c, double s) {
            this.complaint = c;
            this.score = s;
        }
    }
}
