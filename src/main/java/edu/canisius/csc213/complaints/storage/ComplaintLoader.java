package edu.canisius.csc213.complaints.storage;

import com.opencsv.bean.CsvToBeanBuilder;
import edu.canisius.csc213.complaints.model.Complaint;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Handles loading of complaints and embedding data,
 * and returns a fully hydrated list of Complaint objects.
 */
public class ComplaintLoader {

    /**
     * Loads complaints from a CSV file and merges with embedding vectors from a JSONL file.
     *
     * @param csvPath    Resource path to the CSV file
     * @param jsonlPath  Resource path to the JSONL embedding file
     * @return A list of Complaint objects with attached embedding vectors
     * @throws Exception if file reading or parsing fails
     */
    public static List<Complaint> loadComplaintsWithEmbeddings(String csvPath, String jsonlPath) throws Exception {
        InputStream csvStream = ComplaintLoader.class.getResourceAsStream(csvPath);
        if (csvStream == null) {
            throw new IllegalArgumentException("CSV resource not found: " + csvPath);
        }

        InputStream jsonlStream = ComplaintLoader.class.getResourceAsStream(jsonlPath);
        if (jsonlStream == null) {
            throw new IllegalArgumentException("JSONL resource not found: " + jsonlPath);
        }

        InputStreamReader csvReader = new InputStreamReader(csvStream, StandardCharsets.UTF_8);
        List<Complaint> complaints = new CsvToBeanBuilder<Complaint>(csvReader)
                .withType(Complaint.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build()
                .parse();

        Map<Long, double[]> embeddings = EmbeddingLoader.loadEmbeddings(jsonlStream);
        ComplaintMerger.mergeEmbeddings(complaints, embeddings);

        return complaints;
    }
}

