package up.visulog.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;
import up.visulog.gitrawdata.Commit;
import up.visulog.gitrawdata.Filter;

public class CountFilesChanged implements AnalyzerPlugin<String, Integer>{
    public static final String name = "countFilesChanged";
    private final Configuration configuration;
    private Result result;
    private PluginConfig options;
    
    public CountFilesChanged(Configuration config){
        this.configuration = config;
        this.options = config.getPluginConfigs().remove(CountFilesChanged.name);
    }

    Result processLog(List<Commit> gitLog) {
        var result = new Result(this.options);
        for(Commit c : gitLog) {
            c.files.forEach((k, v) -> {
                if(!result.files.containsKey(k)){
                    result.files.put(k, v);
                } else {
                    result.files.replace(k, v+result.files.get(k));
                }
            });
        }
        return result;
    }

    @Override
    public void run() {
        List<Filter> filters = new ArrayList<Filter>();
        for (var options : this.options.getValueOptions().entrySet()){
            try {
                filters.add(Filter.getFilter(options.getKey(), options.getValue()));
            } catch (IllegalArgumentException e) {
            }
        }
        result = processLog(Commit.getFilteredCommits(configuration.getGitRepo(), filters));
    }

    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }
    
    static class Result implements AnalyzerPlugin.Result<String, Integer> {
        private PluginConfig options;
        private HashMap<String, Integer> files;

        Result(PluginConfig options) {
            files = new HashMap<>();
            this.options = options;
        }

        @Override
        public String getPluginName() {
            return CountFilesChanged.name;
        }

        @Override
        public String getId() {
            var uuid = UUID.randomUUID().toString();
            return uuid;
        }

        @Override
        public PluginConfig getPluginOptions() {
            return options;
        }

        @Override
        public String getResultAsString() {
            return files.toString();
        }

        @Override
        public Map<String, Integer> getData() {
            return new HashMap<String, Integer>(files);
        }
    
    }

}
