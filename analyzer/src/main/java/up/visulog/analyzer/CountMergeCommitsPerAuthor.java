package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.Filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CountMergeCommitsPerAuthor implements AnalyzerPlugin<String, Integer> {
    public static final String name = "countMergeCommits";
    private final Configuration configuration;
    private Result result;
    private PluginConfig options;

    public CountMergeCommitsPerAuthor(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
        this.options = generalConfiguration.getPluginConfigs().remove(CountMergeCommitsPerAuthor.name);
    }

    Result processLog(List<Commit> gitLog) {
        var result = new Result(this.options);
        for (var commit : gitLog) {
            if (commit.mergeCommit) {
            	var nb = result.commitsPerAuthor.getOrDefault(commit.author, 0);
            	result.commitsPerAuthor.put(commit.author, nb + 1); 
            }
        }
        return result;
    }

    @Override
    public void run() {
        List<Filter> filters = Filter.getFilters(this.options.getValueOptions());
        result = processLog(Commit.getFilteredCommits(configuration.getGitRepo(), filters));
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    static class Result implements AnalyzerPlugin.Result<String, Integer> {
        private PluginConfig options;
        private final Map<String, Integer> commitsPerAuthor = new HashMap<>(); 

        Result(PluginConfig options) {
            this.options = options;
        }

        @Override
        public String getPluginName() {
            return CountMergeCommitsPerAuthor.name;
        }

        @Override
        public String getId() {
            var uuid = UUID.randomUUID().toString();
            return uuid;
        }

        @Override
        public PluginConfig getPluginOptions() {
            return this.options;
        }

        @Override
        public String getResultAsString() {
            return commitsPerAuthor.toString();
        }

        @Override
        public Map<String, Integer>getData() {
            return new HashMap<>(commitsPerAuthor);
        }
    }
}

