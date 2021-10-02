let chartsSelector = document.querySelector('#charts-selector');
let moduleSelector = document.querySelector('#module-selector');

let chartObject;

let gitData = JSON.parse(localStorage.getItem('gitData'));

document.querySelector('#header').innerText = `Visulog - Charts`;

for(let chart of gitData.map(m => m.options.charts).reduce((a, b) => [...a, ...b]).reduce((a, b) => !Array.isArray(a) ? [a] : a.includes(b) ? a : [...a, b] )) {
    let chartRadio = document.createElement('input');
    let chartLabel = document.createElement('label');
    chartRadio.name = 'chartType';
    chartLabel.innerText = chart;
    chartRadio.value = chart;
    chartRadio.type = 'radio';
    chartRadio.onclick = () => {
        displayCharts();
    }
    chartRadio.checked = chartsSelector.children.length == 0 ? true : false;
    chartsSelector.append(chartLabel);
    chartsSelector.append(chartRadio);
}

for(let module of gitData) {
    let moduleRadio = document.createElement('input');
    moduleRadio.type = 'radio';
    moduleRadio.name = 'modules';
    moduleRadio.value = module.id;
    moduleRadio.onclick = () => {
        displayCharts();
    }
    let moduleLabel = document.createElement('label');
    moduleLabel.innerText = module.options.valueOptions.displayName ? module.options.valueOptions.displayName : module.name;
    moduleSelector.append(moduleLabel);
    moduleSelector.append(moduleRadio);
}

function displayCharts() {
    if(chartObject) chartObject.destroy();
    let chartType;
    for(let chart of chartsSelector) {
        if(chart.checked) chartType = chart.value;
    }
    let module;
    for(let mod of moduleSelector) {
        if(mod.checked) module = gitData.find(m => m.id == mod.value);
    }
    let dataset = {
        label: module.options.valueOptions.displayName ? module.options.valueOptions.displayName : module.name,
        data: Object.values(module.data),
        backgroundColor: module.options.valueOptions.color ? module.options.valueOptions.color : Object.entries(module.data).map(a => randomColor()),
    };
    var ctx = document.getElementById('chart').getContext('2d');
    var chartConfig = {
        type: chartType,
        data: {
            labels: Object.keys(module.data),
            datasets: [dataset]
        }
    };
    chartObject = new Chart(ctx, chartConfig);
}

function randomColor() {
    return `rgba(${Math.floor(Math.random()*255)}, ${Math.floor(Math.random()*255)}, ${Math.floor(Math.random()*255)}, ${Math.random()*0.5+0.5})`
}