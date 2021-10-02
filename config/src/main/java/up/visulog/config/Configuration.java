package up.visulog.config;

import java.nio.file.Path;
import java.util.HashMap;

public class Configuration {

    private final Path gitPath;
    private final HashMap<String, PluginConfig> plugins;
    private boolean indentation = false;
    private String outputFile = "";

    public Configuration(Path gitPath, HashMap<String, PluginConfig> plugins) {
        this.gitPath = gitPath;
        this.plugins = new HashMap<String, PluginConfig>(plugins);
    }

    public Path getGitPath() {
        return gitPath;
    }

    public HashMap<String, PluginConfig> getPluginConfigs() {
        return plugins;
    }

    public void setIndent(boolean i) {
        this.indentation = i;
    }
    
    public void setOutputFile(String s) {
        this.outputFile=s;
    }

    public boolean isIndented() {
        return this.indentation;
    }

    public String outputFile() {
        return this.outputFile;
    }
}
