package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;
import up.visulog.gitrawdata.Commit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CountCommitsPerAuthorPlugin implements AnalyzerPlugin {
    public static final String name = "Count commits";
    private final Configuration configuration;
    private Result result;
    private PluginConfig options;

    public CountCommitsPerAuthorPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
        this.options = generalConfiguration.getPluginConfigs().remove(CountCommitsPerAuthorPlugin.name);
    }

    Result processLog(List<Commit> gitLog) {
        var result = new Result(this.options);
        for (var commit : gitLog) {
            var nb = result.commitsPerAuthor.getOrDefault(commit.author, 0);
            result.commitsPerAuthor.put(commit.author, nb + 1);
        }
        return result;
    }

    @Override
    public void run() {
        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath()));
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {
        private PluginConfig options;
        private final Map<String, Integer> commitsPerAuthor = new HashMap<>();

        Result(PluginConfig options) {
            this.options = options;
        }

        class CommitData {
            private String commiter;
            private int commits;

            CommitData(String commiter, int commits) {
                this.commiter = commiter;
                this.commits = commits;
            }

            public String getCommiter() {
                return commiter;
            }
            public int getCommits() {
                return commits;
            }
        }

        @Override
        public String getPluginName() {
            return CountCommitsPerAuthorPlugin.name;
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
        public List<CommitData>getData() {
            var list = new ArrayList<CommitData>();
            for(var element: commitsPerAuthor.entrySet()) {
                list.add(new CommitData(element.getKey(), element.getValue()));
            }
            return list;
        }
    }
}
