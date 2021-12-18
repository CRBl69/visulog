package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.Filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CountLinesRemovedPerAuthorPlugin implements AnalyzerPlugin<String, Integer> {
    public static final String name = "countLinesRemoved";
    private final Configuration configuration;
    private Result result;
    private PluginConfig options;

    public CountLinesRemovedPerAuthorPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
        this.options = generalConfiguration.getPluginConfigs().remove(CountLinesRemovedPerAuthorPlugin.name);
    }

    Result processLog(List<Commit> gitLog) {
        var result = new Result(this.options);
        for (var commit : gitLog) {
            var nb = result.linePerAuthor.getOrDefault(commit.author, 0);
            result.linePerAuthor.put(commit.author, nb+commit.linesRemoved);
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
        private final Map<String, Integer> linePerAuthor = new HashMap<String, Integer>();

        Result(PluginConfig options) {
            this.options = options;
        }

        @Override
        public String getPluginName() {
            return CountLinesRemovedPerAuthorPlugin.name;
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
        public Map<String, Integer> getData() {
            return new HashMap<String, Integer>(linePerAuthor);
        }
    }
}
