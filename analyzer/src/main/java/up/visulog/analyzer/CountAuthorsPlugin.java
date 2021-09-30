package up.visulog.analyzer;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;


import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.config.PluginConfig;


public class CountAuthorsPlugin implements AnalyzerPlugin {
    MyResult result; 
    Configuration configuration;
    private PluginConfig options;
    public static final String name = "Count authors";

    
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
        result = countAuthors(Commit.parseLogFromCommand(configuration.getGitPath()));
        // TODO Auto-generated method stub

    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    static class MyResult implements AnalyzerPlugin.Result {
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
            authorSet = new HashSet();
        }
        
        @Override
        public String getResultAsString() {
            // TODO Auto-generated method stub
            return authorSet.toString();
        }

        @Override
        public Integer getData() {
            return this.authorSet.size();
        }

    }
}
