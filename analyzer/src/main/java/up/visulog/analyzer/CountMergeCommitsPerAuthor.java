package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;
import up.visulog.gitrawdata.Commit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CountMergeCommitsPerAuthor implements AnalyzerPlugin {
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
            if (commit.isMergeCommit()) {
            	var nb = result.commitsPerAuthor.getOrDefault(commit.author, 0); //
            	result.commitsPerAuthor.put(commit.author, nb + 1); 
            }
        }
        return result;
    }

    @Override
    public void run() {
        result = processLog(Commit.getAllCommits(configuration.getGitRepo()));
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {
        private PluginConfig options;
        private final Map<String, Integer> commitsPerAuthor = new HashMap<>(); 
        public Map<String, Integer> getCommitsPerAuthor() {
            return commitsPerAuthor;
        }

        Result(PluginConfig options) {
            this.options = options;
        }

        @Override
        public String getPluginName() {
            return CountLinesPerAuthorPlugin.name;
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
        public Map<Object, Object>getData() {
            return new HashMap<Object, Object>(commitsPerAuthor);
        }
    }
}

