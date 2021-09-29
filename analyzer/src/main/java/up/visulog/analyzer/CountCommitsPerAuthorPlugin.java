package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CountCommitsPerAuthorPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;

    public CountCommitsPerAuthorPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static Result processLog(List<Commit> gitLog) {
        var result = new Result();
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
        private final Map<String, Integer> commitsPerAuthor = new HashMap<>();

        public String getPluginName() {
            return "countCommits";
        }

        public String getId() {
            var uuid = UUID.randomUUID().toString();
            return uuid;
        }

        public Options getPluginOptions() {
            return new Options()
                .addChart("bars")
                .addChart("pie");
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
