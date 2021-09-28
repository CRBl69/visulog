package up.visulog.analyzer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface AnalyzerPlugin {
    interface Result {
        @JsonIgnore
        String getResultAsString();

        /**
         * This is the actual data that is requested by the user
         * It must be something that converts to JSON, or else,
         * the plugin will crash at runtime
         * @return an object that represents the data of the plugin
         */
        @JsonProperty("data")
        Object getData();

        /**
         * This is useful in order to know by which plugin the
         * data was generated and to know how to deal with it
         * on the frontend
         * @return the name of the plugin
         */
        @JsonProperty("name")
        String getPluginName();
    }

    /**
     * run this analyzer plugin
     */
    void run();

    /**
     * @return the result of this analysis. Runs the analysis first if not already done.
     */
    Result getResult();
}
