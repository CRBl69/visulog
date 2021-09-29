package up.visulog.config;

import java.nio.file.Path;
import java.util.HashMap;

public class Configuration {

    private final Path gitPath;
    private final HashMap<String, PluginConfig> plugins;

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
}
