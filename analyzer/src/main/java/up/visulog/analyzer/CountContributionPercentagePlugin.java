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

public class CountContributionPercentagePlugin implements AnalyzerPlugin<String, Double> {
    public static final String name = "CountContributionPercentage";
    private final Configuration configuration;
    private Result result;
    private PluginConfig options;
    private CountCommitsPerAuthorPlugin cpt;

    public CountContributionPercentagePlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
        this.options = generalConfiguration.getPluginConfigs().remove(CountContributionPercentagePlugin.name);
        this.cpt = new CountCommitsPerAuthorPlugin(generalConfiguration);
    }

    Result processLog(List<Commit> gitLog) {
        Double Somme=0.0;
        var result = new Result(this.options);
        var resultP =  cpt.processLog(gitLog);
        for (var nb : resultP.getData().values()) Somme=Somme+nb;
        for (var item : resultP.getData().entrySet()) {
            result.percentagePerAuthor.put(item.getKey(), (Double.valueOf(item.getKey())*100/Somme));
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

    static class Result implements AnalyzerPlugin.Result<String, Double> {
        private PluginConfig options;
        private final Map<String, Double> percentagePerAuthor = new HashMap<>();

        Result(PluginConfig options) {
            this.options = options;
        }

        @Override
        public String getPluginName() {
            return CountContributionPercentagePlugin.name;
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
            return percentagePerAuthor.toString();
        }

        @Override
        public Map<String, Double>getData() {
            return new HashMap<String, Double>(percentagePerAuthor);
        }
    }
}
