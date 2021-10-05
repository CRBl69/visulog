package up.visulog.cli;

import static spark.Spark.*;

public class ServeFrontend {
    public static void serve(int port, String data) {
        port(port);
        System.out.printf("Server started on http://localhost:%d/", port);
        get("/", (req, res) -> {
            res.header("Content-Type", "text/html");
            return indexHTML;
        });

        get("/style.css", (req, res) -> {
            res.header("Content-Type", "text/css");
            return style;
        });

        get("/script.js", (req, res) -> {
            res.header("Content-Type", "application/javascript");
            return script;
        });

        get("/data.json", (req, res) -> {
            res.header("Content-Type", "application/json");
            return data;
        });
    }
    private static final String indexHTML = "<!DOCTYPE html> <html lang=\"en\"> <head> <meta charset=\"UTF-8\"> <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"> <title>Visulog - Module</title> <link rel=\"stylesheet\" href=\"style.css\"> <script src=\"https://cdn.jsdelivr.net/npm/chart.js@3.5.1/dist/chart.min.js\"></script> </head> <body> <div id=\"header\"> Visulog </div> <div id=\"charts-selector-container\"> <form id=\"charts-selector\"></form> </div> <div id=\"module-selector-container\"> <form id=\"module-selector\"></form> </div> <canvas id=\"chart\"></canvas> <script src=\"script.js\"></script> </body> </html>";
    private static final String script = "fetch('data.json').then(res => res.text()).then(res => localStorage.setItem('gitData', res));let chartsSelector = document.querySelector('#charts-selector'); let moduleSelector = document.querySelector('#module-selector'); let chartObject; let gitData = JSON.parse(localStorage.getItem('gitData')); document.querySelector('#header').innerText = `Visulog - Charts`; for(let chart of gitData.map(m => m.options.charts).reduce((a, b) => [...a, ...b]).reduce((a, b) => !Array.isArray(a) ? [a] : a.includes(b) ? a : [...a, b] )) { let chartRadio = document.createElement('input'); let chartLabel = document.createElement('label'); chartRadio.name = 'chartType'; chartLabel.innerText = chart; chartRadio.value = chart; chartRadio.type = 'radio'; chartRadio.onclick = () => { displayCharts(); }; chartRadio.checked = chartsSelector.children.length == 0 ? true : false; chartsSelector.append(chartLabel); chartsSelector.append(chartRadio); } for(let module of gitData) { let moduleRadio = document.createElement('input'); moduleRadio.type = 'radio'; moduleRadio.name = 'modules'; moduleRadio.value = module.id; moduleRadio.onclick = () => { displayCharts(); }; let moduleLabel = document.createElement('label'); moduleLabel.innerText = module.options.valueOptions.displayName ? module.options.valueOptions.displayName : module.name; moduleSelector.append(moduleLabel); moduleSelector.append(moduleRadio); }; function displayCharts() { if(chartObject) chartObject.destroy(); let chartType; for(let chart of chartsSelector) { if(chart.checked) chartType = chart.value; }; let module; for(let mod of moduleSelector) { if(mod.checked) module = gitData.find(m => m.id == mod.value); } let dataset = { label: module.options.valueOptions.displayName ? module.options.valueOptions.displayName : module.name, data: Object.values(module.data), backgroundColor: module.options.valueOptions.color ? module.options.valueOptions.color : Object.entries(module.data).map(a => randomColor()), }; var ctx = document.getElementById('chart').getContext('2d'); var chartConfig = { type: chartType, data: { labels: Object.keys(module.data), datasets: [dataset] } }; chartObject = new Chart(ctx, chartConfig); } function randomColor() { return `rgba(${Math.floor(Math.random()*255)}, ${Math.floor(Math.random()*255)}, ${Math.floor(Math.random()*255)}, ${Math.random()*0.5+0.5})` }";
    private static final String style = "body { margin: 0; } #header { width: 100vw; height: 1em; background-color: lightskyblue; font-size: xx-large; text-align: center; padding: .5em; font-family: 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif; } #container { display: grid; place-items: center; height: 50vh; justify-items: center; } #json-form { border-radius: 2rem; border: 1px solid lightskyblue; padding: 2rem; } #charts-selector-container { width: 100vw; display: grid; place-items: center; } #chart { max-width: 90vw; }";
}
