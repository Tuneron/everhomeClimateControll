var stompClient = null;
var socket = null;
let humrows = [];

function connect() {
    socket = new SockJS('/stomp');
    stompClient = Stomp.over(socket);
    stompClient.reconnect_delay = 3000;
    stompClient.connect({}, frame => {
        console.log('Connected: ' + frame);

        stompClient.subscribe('/topic/humidity', hum => {
        var humidity = JSON.parse(hum.body);
        updateHumidity(humidity.value);
        });

        stompClient.subscribe('/topic/temperature', t1 => {
            var temperature1 = JSON.parse(t1.body);
            updateTemperature(temperature1.value);
        });

        stompClient.subscribe('/topic/set_power', stP => {
             var setPower = JSON.parse(stP.body);
             updateSetPower(setPower.value);
        });

        stompClient.subscribe('/topic/set_temperature', st => {
            var setTemperature = JSON.parse(st.body);
            updateSetTemperature(setTemperature.value);
        });

        stompClient.subscribe('/topic/set_conditioner_mode', setCondMode => {
             var setConditionerMode = JSON.parse(setCondMode.body);
             updateSetConditionerMode(setConditionerMode.value);
        });

        stompClient.subscribe('/topic/set_fan_speed', setFanSp => {
             var setFanSpeed = JSON.parse(setFanSp.body);
             updateSetFanSpeed(setFanSpeed.value);
        });

        stompClient.subscribe('/topic/set_fluger', setFl => {
             var setFluger = JSON.parse(setFl.body);
             updateSetFluger(setFluger.value);
        });

        stompClient.subscribe('/topic/set_custom', setCust => {
             var setCustom = JSON.parse(setCust.body);
             updateSetCustom(setCustom.value);
        });

        stompClient.subscribe('/topic/set_setting', setSetr => {
             var setSetting = JSON.parse(setSetr.body);
             updateSetSetting(setSetting.value);
        });

        stompClient.subscribe('/topic/set_climate_mode', setClimMode => {
             var setClimateMode = JSON.parse(setClimMode.body);
             updateSetClimateMode(setClimateMode.value);
        });

        stompClient.subscribe('/topic/set_season', setSea => {
             var setSeason = JSON.parse(setSea.body);
             updateSetSeason(setSeason.value);
        });

        stompClient.subscribe('/topic/set_radiator', setRad => {
             var setRadiator = JSON.parse(setRad.body);
             updateSetRadiator(setRadiator.value);
        });

        stompClient.subscribe('/topic/set_humidifier', setHum => {
             var setHumidifier = JSON.parse(setHum.body);
             updateSetHumidifier(setHumidifier.value);
        });

        stompClient.subscribe('/topic/set_command', setCom => {
             var setCommand = JSON.parse(setCom.body);
             updateSetCommand(setCommand);
        });

        stompClient.subscribe('/topic/set_eco', setEc => {
             var setEco= JSON.parse(setEc.body);
             updateSetEco(setEco.value);
        });

        stompClient.subscribe('/topic/set_window', setWin => {
             var setWindow= JSON.parse(setWin.body);
             updateSetWindow(setWindow.value);
        });

        stompClient.subscribe('/topic/set_recuperator', setRec => {
             var setRecuperator= JSON.parse(setRec.body);
             updateSetRecuperator(setRecuperator.value);
        });

        stompClient.subscribe('/topic/set_water_floor', setWF => {
             var setWaterFloor= JSON.parse(setWF.body);
             updateSetWaterFloor(setWaterFloor.value);
        });

        stompClient.subscribe('/topic/set_electric_floor', setEF => {
             var setElectricFloor= JSON.parse(setEF.body);
             updateSetElectricFloor(setElectricFloor.value);
        });

        stompClient.subscribe('/topic/set_outside_conditions', setOutCond => {
             var setOutsideConditions= JSON.parse(setOutCond.body);
             updateSetOutsideConditions(setOutsideConditions.value);
        });

        stompClient.subscribe('/topic/set_silence', setSil => {
             var setSilence= JSON.parse(setSil.body);
             updateSetSilence(setSilence.value);
        });

        stompClient.subscribe('/topic/set_night', setNig => {
             var setNight= JSON.parse(setNig.body);
             updateSetNight(setNight.value);
        });

        stompClient.subscribe('/topic/set_enable_eco_radiator', setEnEcoRad => {
             var setEnableEcoRadiator= JSON.parse(setEnEcoRad.body);
             updateSetEnableEcoRadiator(setEnableEcoRadiator.value);
        });

        stompClient.subscribe('/topic/connection', v => {
            var valve = JSON.parse(v.body);
            updateConnection(valve.value);
        });
    });
}

function updateHumidity(value) {
    $("#humidity").text(value + "%");
}
function updateTemperature(value) {
    $("#temperature").text(value + "C");
}
function updateSetPower(value) {
    $("#set_power").text(value);
}
function updateSetTemperature(value) {
    $("#set_temperature").text(value);
}
function updateSetConditionerMode(value) {
    $("#set_conditioner_mode").text(value);
}
function updateSetFanSpeed(value){
    $("#set_fan_speed").text(value);
}
function updateSetFluger(value){
    $("#set_fluger").text(value);
}
function updateSetCustom(value){
    $("#set_custom").text(value);
}
function updateSetSetting(value){
    $("#set_setting").text(value);
}
function updateSetClimateMode(value){
    $("#set_climate_mode").text(value);
}
function updateSetSeason(value){
    $("#set_season").text(value);
}
function updateSetRadiator(value){
    $("#set_radiator").text(value);
}
function updateSetHumidifier(value){
    $("#set_humidifier").text(value);
}
function updateSetEco(value){
    $("#set_eco").text(value);
}
function updateSetWindow(value){
    $("#set_window").text(value);
}
function updateSetRecuperator(value){
    $("#set_recuperator").text(value);
}
function updateSetWaterFloor(value){
    $("#set_water_floor").text(value);
}
function updateSetElectricFloor(value){
    $("#set_electric_floor").text(value);
}
function updateSetOutsideConditions(value){
    $("#set_outside_conditions").text(value);
}
function updateSetSilence(value){
    $("#set_silence").text(value);
}
function updateSetNight(value){
    $("#set_night").text(value);
}
function updateSetEnableEcoRadiator(value){
    $("#set_enable_eco_radiator").text(value);
}

function updateSetCommand(cmd){
    console.log("ETOT CMD ->>>"+cmd);
    console.log("1 ->>>"+cmd.value);
    console.log("2 ->>>"+cmd.currentTemperature);
    $("#set_command").text(cmd.value);
    $("#set_command").text(cmd.currentTemperature);
    $("#set_command").text(cmd.currentHumidity);
    $("#set_command").text(cmd.conditionerPower);

}
function updateConnection(value) {
    $("#connection").text(value);
}

function setTemperature(value, dir) {
    stompClient.send("/app/setTemperature/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#set_temperature").text())
            }
        )
    );
}

function setPower(value, dir) {
    stompClient.send("/app/setPower/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#set_power").text())
            }
        )
    );
}

function setConditionerMode(value, dir) {
    stompClient.send("/app/setConditionerMode/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#set_conditioner_mode").text())
            }
        )
    );
}

function setFanSpeed(value, dir){
    stompClient.send("/app/setFanSpeed/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#set_fan_speed").text())
            }
        )
    );
}

function setFluger(value, dir){
    stompClient.send("/app/setFluger/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#set_fluger").text())
            }
        )
    );
}

function setCustom(value, dir){
    stompClient.send("/app/setCustom/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#set_custom").text())
            }
        )
    );
}

function setSetting(value, dir){
    stompClient.send("/app/setSetting/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#set_setting").text())
            }
        )
    );
}

function setClimateMode(value, dir){
    stompClient.send("/app/setClimateMode/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#set_climate_mode").text())
            }
        )
    );
}

function setSeason(value, dir){
    stompClient.send("/app/setSeason/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#set_season").text())
            }
        )
    );
}

function setRadiator(value, dir){
    stompClient.send("/app/setRadiator/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#set_radiator").text())
            }
        )
    );
}

function setHumidifier(value, dir){
    stompClient.send("/app/setHumidifier/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#set_humidifier").text())
            }
        )
    );
}

function setCommand(value, dir){
    stompClient.send("/app/setCommand/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#set_command").text())
            }
        )
    );
}

function setEco(value, dir){
    stompClient.send("/app/setEco/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#set_eco").text())
            }
        )
    );
}

function setWindow(value, dir){
    stompClient.send("/app/setWindow/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#set_window").text())
            }
        )
    );
}

function setRecuperator(value, dir){
    stompClient.send("/app/setRecuperator/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#set_recuperator").text())
            }
        )
    );
}

function setWaterFloor(value, dir){
    stompClient.send("/app/setWaterFloor/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#set_water_floor").text())
            }
        )
    );
}

function setElectricFloor(value, dir){
    stompClient.send("/app/setElectricFloor/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#set_electric_floor").text())
            }
        )
    );
}

function setOutsideConditions(value, dir){
    stompClient.send("/app/setOutsideConditions/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#set_outside_conditions").text())
            }
        )
    );
}

function setSilence(value, dir){
    stompClient.send("/app/setSilence/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#set_silence").text())
            }
        )
    );
}

function setNight(value, dir){
    stompClient.send("/app/setNight/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#set_night").text())
            }
        )
    );
}

function setEnableEcoRadiator(value, dir){
    stompClient.send("/app/setEnableEcoRadiator/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#set_enable_eco_radiator").text())
            }
        )
    );
}

$(function () {
    $(document).ready(() => {
        connect();
    });
    $("#inc").click(() => {
        setTemperature($("#set_temperature").val(), "inc");
    });
    $("#dec").click(() => {
        setTemperature($("#set_temperature").val(), "dec");
    });
    $("#onPower").click(() => {
        setPower($("#set_power").val(), "onPower");
    });
    $("#offPower").click(() => {
        setPower($("#set_power").val(), "offPower");
    });
    $("#incConditionerMode").click(() => {
        setConditionerMode($("#set_conditioner_mode").val(), "incConditionerMode");
    });
    $("#decConditionerMode").click(() => {
        setConditionerMode($("#set_conditioner_mode").val(), "decConditionerMode");
    });
    $("#incFanSpeed").click(() => {
        setFanSpeed($("#set_fan_speed").val(), "incFanSpeed");
    });
    $("#decFanSpeed").click(() => {
        setFanSpeed($("#set_fan_speed").val(), "decFanSpeed");
    });
    $("#incFluger").click(() => {
        setFluger($("#set_fluger").val(), "incFluger");
    });
    $("#decFluger").click(() => {
        setFluger($("#set_fluger").val(), "decFluger");
    });
    $("#incCustom").click(() => {
        setCustom($("#set_custom").val(), "incCustom");
    });
    $("#decCustom").click(() => {
        setCustom($("#set_custom").val(), "decCustom");
    });
    $("#incSetting").click(() => {
        setSetting($("#set_setting").val(), "incSetting");
    });
    $("#decSetting").click(() => {
        setSetting($("#set_setting").val(), "decSetting");
    });
    $("#incClimateMode").click(() => {
        setClimateMode($("#set_climate_mode").val(), "incClimateMode");
    });
    $("#decClimateMode").click(() => {
        setClimateMode($("#set_climate_mode").val(), "decClimateMode");
    });
    $("#incSeason").click(() => {
        setSeason($("#set_season").val(), "incSeason");
    });
    $("#decSeason").click(() => {
        setSeason($("#set_season").val(), "decSeason");
    });
    $("#incRadiator").click(() => {
        setRadiator($("#set_radiator").val(), "incRadiator");
    });
    $("#decRadiator").click(() => {
        setRadiator($("#set_radiator").val(), "decRadiator");
    });
    $("#incHumidifier").click(() => {
        setHumidifier($("#set_humidifier").val(), "incHumidifier");
    });
    $("#decHumidifier").click(() => {
        setHumidifier($("#set_humidifier").val(), "decHumidifier");
    });
    $("#sendCommand").click(() => {
        setCommand($("#set_command").val(), "sendCommand");
    });
    $("#incEco").click(() => {
        setEco($("#set_eco").val(), "incEco");
    });
    $("#decEco").click(() => {
        setEco($("#set_eco").val(), "decEco");
    });
    $("#incWindow").click(() => {
        setWindow($("#set_window").val(), "incWindow");
    });
    $("#decWindow").click(() => {
        setWindow($("#set_window").val(), "decWindow");
    });
    $("#incRecuperator").click(() => {
        setRecuperator($("#set_recuperator").val(), "incRecuperator");
    });
    $("#decRecuperator").click(() => {
        setRecuperator($("#set_recuperator").val(), "decRecuperator");
    });
    $("#incWaterFloor").click(() => {
        setWaterFloor($("#set_water_floor").val(), "incWaterFloor");
    });
    $("#decWaterFloor").click(() => {
        setWaterFloor($("#set_water_floor").val(), "decWaterFloor");
    });
    $("#incElectricFloor").click(() => {
        setElectricFloor($("#set_electric_floor").val(), "incElectricFloor");
    });
    $("#decElectricFloor").click(() => {
        setElectricFloor($("#set_electric_floor").val(), "decElectricFloor");
    });
    $("#incOutsideConditions").click(() => {
        setOutsideConditions($("#set_outside_conditions").val(), "incOutsideConditions");
    });
    $("#decOutsideConditions").click(() => {
        setOutsideConditions($("#set_outside_conditions").val(), "decOutsideConditions");
    });
    $("#incSilence").click(() => {
        setSilence($("#set_silence").val(), "incSilence");
    });
    $("#decSilence").click(() => {
        setSilence($("#set_silence").val(), "decSilence");
    });
    $("#incNight").click(() => {
        setNight($("#set_night").val(), "incNight");
    });
    $("#decNight").click(() => {
        setNight($("#set_night").val(), "decNight");
    });
    $("#incEnableEcoRadiator").click(() => {
        setEnableEcoRadiator($("#set_enable_eco_radiator").val(), "incEnableEcoRadiator");
    });
    $("#decEnableEcoRadiator").click(() => {
        setEnableEcoRadiator($("#set_enable_eco_radiator").val(), "decEnableEcoRadiator");
    });
});

$.getScript("https://www.gstatic.com/charts/loader.js", function(){
    google.charts.load('current', {'packages':['corechart']});
    google.charts.setOnLoadCallback(drawChart);
    function drawChart() {
        var request = new XMLHttpRequest();
        request.open('GET', '/api/temperature/day', true);
        request.onload = function() {
            var data = new google.visualization.DataTable();
            var rows = [];
            data.addColumn('datetime', 'Time');
            data.addColumn('number', 'Value');
            var record = JSON.parse(this.response)
            record.forEach(v => {
                console.log(v);
                rows.push([new Date(v.time+'Z'), v.value]);
            });
            data.addRows(rows);
            var options = {
              title: 'Temperature',
              curveType: 'function',
              colors: ['#ff623b'],
              legend: { position: 'bottom' }
            };
            var chart = new google.visualization.LineChart(document.getElementById('chartT'));
            chart.draw(data, options);
            console.log(JSON.stringify(rows));
            console.log(rows);
            console.log(data);
        }
        request.send();
    }
});

function loadHumidity(){
    console.log("FUNCTION START");
     var requestH = new XMLHttpRequest();
     console.log("1");
     requestH.open('GET', '/api/humidity/day', true);
     console.log("2");
     requestH.onload = function() {
console.log("3");
         var recordH = JSON.parse(this.response);
         console.log("4");
         console.log("Start hum");
         recordH.forEach(v => {
             console.log(v.value);
             humrows.push(parseFloat(v.value));
             });
         console.log("End hum");
         }
         requestH.send();
     return humrows;
    }

$.getScript("https://www.gstatic.com/charts/loader.js", function(){
    google.charts.load('current', {'packages':['corechart']});
    google.charts.setOnLoadCallback(drawChart);
    console.log(loadHumidity());
    function drawChart() {

        var request = new XMLHttpRequest();
        request.open('GET', '/api/temperature/day', true);
        request.onload = function() {
            var data = new google.visualization.DataTable();
            var rows = [];
            var count = 0;
            data.addColumn('datetime', 'Time');
            data.addColumn('number', 'Temperature');
            data.addColumn('number', 'Humidity');
            var record = JSON.parse(this.response)
            console.log("Start final temp and hum");
            record.forEach(v => {
                console.log(v.value);
                rows.push([new Date(v.time+'Z'), v.value, parseFloat(humrows[count])]);
                count++;
            });
            console.log("End");
            data.addRows(rows);
            var options = {
              title: 'Temperature and humidity',
              curveType: 'function',
              colors: ['#ff623b', '#264ee1'],
              legend: { position: 'bottom' }
            };
            var chart = new google.visualization.LineChart(document.getElementById('chartH'));
            chart.draw(data, options);
            console.log(JSON.stringify(rows));
            console.log(rows);
            console.log(data);
        }
        request.send();
    }
});