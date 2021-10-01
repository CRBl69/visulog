package up.visulog.cli;

import up.visulog.analyzer.Analyzer;
import up.visulog.analyzer.CountCommitsPerAuthorPlugin;
import up.visulog.analyzer.CountAuthorsPlugin;
import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;


class Arguments {
    @Parameter(description = "Git path")
    private String gitPath = "";

    @Parameter(names = { "-p", "--addPlugin" }, description = "Selectionner un plugin a ajouter")
    private List<String> plugins;

    @Parameter(names = { "-c", "--loadConfigFile" }, description = "Charger un fichier de configuration")
    private String configFile = "";

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

    public String getConfigFile() {
        return this.configFile;
    }
}

public class CLILauncher {


    public static void main(String[] args) {
        var config = makeConfigFromCommandLineArgs(args);
        if (config.isPresent()) {
            var analyzer = new Analyzer(config.get());
            var results = analyzer.computeResults();
            System.out.println(results.toJSON(true));
        } else displayHelpAndExit();
    }

//    ['.', '--addPlugin=countCommits', '--addPlugin=myPlugin']

    static Optional<Configuration> makeConfigFromCommandLineArgs(String[] args) {
        var plugins = new HashMap<String, PluginConfig>();
        var arguments = new Arguments();
        Object yamlObject = new Object();
        JCommander.newBuilder()
            .addObject(arguments)
            .build()
            .parse(args);

        Path path = Paths.get(".");

        if(!arguments.getConfigFile().equals("")) {
            try {
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                File myObj = new File(arguments.getConfigFile());
                Scanner myReader = new Scanner(myObj);
                String yamlFile = "";
                while (myReader.hasNextLine()) {
                    yamlFile += myReader.nextLine() + "\n";
                }
                myReader.close();
                yamlObject = mapper.readValue(yamlFile, ConfigFile.class);
                ConfigFile cfg = (ConfigFile)yamlObject;
                if(cfg.path != null) {
                    path = Paths.get(cfg.path);
                }

                if(!cfg.plugins.isEmpty()){
                    for(var plugin : cfg.plugins){
                        var pluginConfig = new PluginConfig();
                        if(plugin.options != null) {
                            if(plugin.options.charts != null) {
                                for(var chart : plugin.options.charts){
                                    pluginConfig.addChart(chart);
                                }
                            }
                            if(plugin.options.valueOptions != null) {
                                for(var option : plugin.options.valueOptions.entrySet()){
                                    pluginConfig.addValueOption(option.getKey(), option.getValue());
                                }
                            }
                            if(plugin.options.toggleOptions != null) {
                                for(var option : plugin.options.toggleOptions){
                                    pluginConfig.addToggledOption(option);
                                }
                            }
                        }
                        plugins.put(plugin.name, pluginConfig);
                    }
                }
            } catch (JsonMappingException e){
                e.printStackTrace();
                System.exit(1);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                System.exit(1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        if(!arguments.getGitPath().equals("")) {
            path = Paths.get(arguments.getGitPath());
        }

        
        for (var plugin : arguments.getPlugins()) {
            switch (plugin) {
                case "countCommits":
                    plugins.put(CountCommitsPerAuthorPlugin.name, new PluginConfig().addChart("bars"));
                    break;
                case "countAuthors":
                    plugins.put(CountAuthorsPlugin.name, new PluginConfig().addChart("bars"));
                    break;
            }
        }
        return Optional.of(new Configuration(path, plugins));
    }

    private static void displayHelpAndExit() {
        System.out.println("Wrong command...");
        //TODO: print the list of options and their syntax
        System.exit(0);
    }
}
