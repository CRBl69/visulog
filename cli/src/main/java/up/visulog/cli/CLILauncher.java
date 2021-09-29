package up.visulog.cli;

import up.visulog.analyzer.Analyzer;
import up.visulog.analyzer.CountCommitsPerAuthorPlugin;
import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

class Arguments {
    @Parameter(description = "Git path")
    private String gitPath = ".";

    @Parameter(names = { "-p", "--addPlugin" }, description = "Selectionner un plugin a ajouter")
    private List<String> plugins;

    @Parameter(names = { "-c", "--loadConfigFile" }, description = "Charger un fichier de configuration")
    private String configFile;

    @Parameter(names = { "-s", "--justSaveConfigFile" }, description = "Charger un fichier de configuration")
    private boolean justSave;

    public List<String> getPlugins() {
        if(this.plugins != null)
            return new ArrayList<String>(this.plugins);
        else
            return new ArrayList<String>();
    }

    public String getGitPath() {
        return this.gitPath;
    }
}

public class CLILauncher {

    public static void main(String[] args) {
        var config = makeConfigFromCommandLineArgs(args);
        if (config.isPresent()) {
            var analyzer = new Analyzer(config.get());
            var results = analyzer.computeResults();
            System.out.println(results.toJSON());
        } else displayHelpAndExit();
    }

    static Optional<Configuration> makeConfigFromCommandLineArgs(String[] args) {
        var plugins = new HashMap<String, PluginConfig>();
        var arguments = new Arguments();
        JCommander.newBuilder()
            .addObject(arguments)
            .build()
            .parse(args);
        for (var plugin : arguments.getPlugins()) {
            switch (plugin) {
                case "countCommits":
                    plugins.put(CountCommitsPerAuthorPlugin.name, new PluginConfig().addChart("bars"));
                    break;
            }
        }
        Path path = Paths.get(arguments.getGitPath());
        return Optional.of(new Configuration(path, plugins));
    }

    private static void displayHelpAndExit() {
        System.out.println("Wrong command...");
        //TODO: print the list of options and their syntax
        System.exit(0);
    }
}
