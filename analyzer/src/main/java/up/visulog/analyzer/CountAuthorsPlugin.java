package up.visulog.analyzer;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;


import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.config.PluginConfig;


public class CountAuthorsPlugin implements AnalyzerPlugin<String, Integer> {
    MyResult result; 
    Configuration configuration;
    private PluginConfig options;
    public static final String name = "countAuthors";

    public CountAuthorsPlugin(Configuration generalConfiguration) {
        this.options = generalConfiguration.getPluginConfigs().remove(CountAuthorsPlugin.name);
        this.configuration = generalConfiguration;
    }

    MyResult countAuthors(List<Commit> log) {
        var result = new MyResult(this.options);

        for (var commit : log) {
            result.authorSet.add(commit.author);
        }
        return result;
    }

    @Override
    public void run() {
        result = countAuthors(Commit.getAllCommits(configuration.getGitRepo()));
    }

    @Override
    public MyResult getResult() {
        if (result == null) run();
        return result;
    }

    static class MyResult implements AnalyzerPlugin.Result<String, Integer> {
        HashSet<String> authorSet;
        private PluginConfig options;

        MyResult(PluginConfig options) {
            this.authorSet = new HashSet<String> ();
            this.options = options;
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
        public String getPluginName() {
            return CountAuthorsPlugin.name;
        }

        public MyResult() {
            authorSet = new HashSet<String>();
        }

        @Override
        public String getResultAsString() {
            return authorSet.toString();
        }

        @Override
        public Map<String, Integer> getData() {
            var map = new HashMap<String, Integer>();
            map.put("totalAuthors", this.authorSet.size());
            return map;
        }

    }
}
