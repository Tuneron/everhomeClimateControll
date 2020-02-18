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