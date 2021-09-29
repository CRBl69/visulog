package up.visulog.config;

import java.util.ArrayList;
import java.util.HashMap;
import com.fasterxml.jackson.annotation.JsonIgnore;

// TODO: define what this type should be (probably a Map: settingKey -> settingValue)
public class PluginConfig {
    private ArrayList<String> charts;
    private ArrayList<String> toggledOptions;
    private ArrayList<HashMap<String, String>> valueOptions;

    public PluginConfig() {
        this.charts = new ArrayList<String>();
        this.toggledOptions = new ArrayList<String>();
        this.valueOptions = new ArrayList<HashMap<String, String>>();
    }

    public ArrayList<String> getCharts() {
        return new ArrayList<String>(this.charts);
    }

    @JsonIgnore
    public PluginConfig addChart(String chartName) {
        this.charts.add(chartName);
        return this;
    }

    public ArrayList<String> getToggledOptions() {
        return new ArrayList<String>(this.toggledOptions);
    }

    @JsonIgnore
    public PluginConfig addToggledOption(String optionName) {
        this.toggledOptions.add(optionName);
        return this;
    }

    public ArrayList<HashMap<String, String>> getValueOptions() {
        return new ArrayList<HashMap<String, String>>(this.valueOptions);
    }

    @JsonIgnore
    public PluginConfig addValueOption(String optionName, String optionValue) {
        var hs = new HashMap<String, String>();
        hs.put(optionName, optionValue);
        this.valueOptions.add(hs);
        return this;
    }
}
