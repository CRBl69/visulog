package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;
import up.visulog.gitrawdata.Commit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CountLinesPerAuthorPlugin implements AnalyzerPlugin {
    public static final String name = "countLines";
    private final Configuration configuration;
    private Result result;
    private PluginConfig options;

    public CountLinesPerAuthorPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
        this.options = generalConfiguration.getPluginConfigs().remove(CountLinesPerAuthorPlugin.name);
    }

    Result processLog(List<Commit> gitLog) {
        var result = new Result(this.options);
        for (var commit : gitLog) {
            var nb = result.linePerAuthor.getOrDefault(commit.author, 0);
            result.linePerAuthor.put(commit.author, nb+commit.linesAdded);
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
        private final Map<String, Integer> linePerAuthor = new HashMap<String, Integer>();

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
            return linePerAuthor.toString();
        }

        @Override
        public Map<Object, Object>getData() {
            return new HashMap<Object, Object>(linePerAuthor);
        }
    }
}

