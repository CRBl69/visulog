package up.visulog.analyzer;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import up.visulog.config.PluginConfig;

public interface AnalyzerPlugin {
    interface Result<T1,T2> {

        @JsonIgnore
        String getResultAsString();

        /**
         * This is the actual data that is requested by the user
         * It must be something that converts to JSON, or else,
         * the plugin will crash at runtime
         * @return an object that represents the data of the plugin
         */
        @JsonProperty("data")
        Map<T1, T2> getData();

        /**
         * This is useful in order to know by which plugin the
         * data was generated and to know how to deal with it
         * on the frontend
         * @return the name of the plugin
         */
        @JsonProperty("name")
        String getPluginName();

        /**
         * This is useful in order to know how to render the
         * chart on the front end
         * @return the options of the plugin
         */
        @JsonProperty("options")
        PluginConfig getPluginOptions();

        /**
         * Generates an unique identifier for each requested
         * plugin, in order to differentiate them in the
         * frontend
         * @return a UUID as a string
         */
        @JsonProperty("id")
        String getId();
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
