package up.visulog.analyzer;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.Filter;
import up.visulog.config.PluginConfig;


public class CountLinesOverTimePlugin implements AnalyzerPlugin<Long, Map<String, Integer>> {
    MyResult result; 
    Configuration configuration;
    private PluginConfig options;
    public static final String name = "countLinesOverTime";

    public CountLinesOverTimePlugin(Configuration generalConfiguration) {
        this.options = generalConfiguration.getPluginConfigs().remove(CountLinesOverTimePlugin.name);
        this.configuration = generalConfiguration;
    }

    MyResult countLinesOverTime(List<Commit> log) {
        var result = new MyResult(this.options);
        for (var commit : log) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("added", commit.linesAdded);
            map.put("deleted", commit.linesRemoved);
            result.commits.put(commit.date.toEpochSecond(ZoneOffset.UTC), map);
        }
        return result;
    }

    @Override
    public void run() {
        List<Filter> filters = Filter.getFilters(this.options.getValueOptions());
        result = countLinesOverTime(Commit.getFilteredCommits(configuration.getGitRepo(), filters));
    }

    @Override
    public MyResult getResult() {
        if (result == null) run();
        return result;
    }

    static class MyResult implements AnalyzerPlugin.Result<Long, Map<String, Integer>> {
        HashMap<Long, Map<String, Integer>> commits;
        private PluginConfig options;

        MyResult(PluginConfig options) {
            this.commits = new HashMap<Long, Map<String, Integer>> ();
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
            commits = new HashMap<Long, Map<String, Integer>>();
        }

        @Override
        public String getResultAsString() {
            return commits.toString();
        }

        @Override
        public Map<Long, Map<String, Integer>> getData() {
            var map = new HashMap<Long, Map<String, Integer>>(commits);
            return map;
        }

    }
}
