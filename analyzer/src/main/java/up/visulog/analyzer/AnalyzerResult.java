package up.visulog.analyzer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class AnalyzerResult {
    public List<AnalyzerPlugin.Result> getSubResults() {
        return subResults;
    }

    private final List<AnalyzerPlugin.Result> subResults;

    public AnalyzerResult(List<AnalyzerPlugin.Result> subResults) {
        this.subResults = subResults;
    }

    @Override
    public String toString() {
        return subResults
            .stream()
            .map(AnalyzerPlugin.Result::getResultAsString)
            .reduce("", (acc, cur) -> acc + "\n" + cur);
    }

    public String toJSON() {
        try {
            return new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(
                subResults
                    .stream()
                    .collect(Collectors.toList())
            );
        } catch(JsonProcessingException e) {
            System.err.println("Error while stringifying JSON");
            e.printStackTrace();
            System.exit(1);
        }
        return "[]";
    }

    public void toJSONFile(String filename) {
        try {
            FileWriter file = new FileWriter(filename);
            file.write(this.toJSON());
            file.close();
        } catch (IOException e) {
            System.err.printf("Could not output JSON to %s\n", filename);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
