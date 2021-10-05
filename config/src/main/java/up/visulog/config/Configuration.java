package up.visulog.config;

import java.nio.file.Path;
import java.util.HashMap;

public class Configuration {
    private final int port;
    private final Path gitPath;
    private final HashMap<String, PluginConfig> plugins;
    private final boolean indent;
    private final String outputFile;

    public Configuration(Path gitPath, HashMap<String, PluginConfig> plugins, int port, String outputFile, boolean indent) {
        this.gitPath = gitPath;
        this.plugins = new HashMap<String, PluginConfig>(plugins);
        this.indent = indent;
        this.port = port;
        this.outputFile = outputFile;
    }

    public Path getGitPath() {
        return gitPath;
    }

    public HashMap<String, PluginConfig> getPluginConfigs() {
        return plugins;
    }

    public int getPort() {
        return this.port;
    }

    public boolean isIndented() {
        return this.indent;
    }

    public String outputFile() {
        return this.outputFile;
    }
}
