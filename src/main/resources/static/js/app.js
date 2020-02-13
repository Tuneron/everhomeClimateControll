var stompClient = null;
var socket = null;

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

        stompClient.subscribe('/topic/myTemperature', myT => {
        var myTemperature = JSON.parse(myT.body);
        updateMyTemperature(myTemperature.value);
        });

        stompClient.subscribe('/topic/temperature1', t1 => {
            var temperature1 = JSON.parse(t1.body);
            updateTemperature1(temperature1.value);
        });

        stompClient.subscribe('/topic/temperature2', t2 => {
            var temperature2 = JSON.parse(t2.body);
            updateTemperature2(temperature2.value);
        });

        stompClient.subscribe('/topic/set_temperature', st => {
            var setTemperature = JSON.parse(st.body);
            updateSetTemperature(setTemperature.value);
        });

        stompClient.subscribe('/topic/mySet_temperature', mySt => {
             var mySetTemperature = JSON.parse(mySt.body);
             updateMySetTemperature(mySetTemperature.value);
        });

        stompClient.subscribe('/topic/valve', v => {
            var valve = JSON.parse(v.body);
            updateValve(valve.value);
        });

        stompClient.subscribe('/topic/connection', v => {
            var valve = JSON.parse(v.body);
            updateConnection(valve.value);
        });
    });
}

function updateHumidity(value) {
    $("#humidity").text(value);
}
function updateMyTemperature(value) {
    $("#myTemperature").text(value);
}
function updateTemperature1(value) {
    $("#temperature1").text(value);
}
function updateTemperature2(value) {
    $("#temperature2").text(value);
}
function updateSetTemperature(value) {
    $("#set_temperature").text(value);
}
function updateMySetTemperature(value) {
    $("#mySet_temperature").text(value);
}
function updateValve(value) {
    $("#valve").text(value);
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

function mySetTemperature(value, dir) {
    stompClient.send("/app/mySetTemperature/" + dir, {},
        JSON.stringify(
            {
                value: parseFloat($("#mySet_temperature").text())
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
    $("#myInc").click(() => {
        setTemperature($("#mySet_temperature").val(), "myInc");
    });
    $("#myDec").click(() => {
        setTemperature($("#mySet_temperature").val(), "myDec");
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

$.getScript("https://www.gstatic.com/charts/loader.js", function(){
    google.charts.load('current', {'packages':['corechart']});
    google.charts.setOnLoadCallback(drawChart);
    function drawChart() {
        var request = new XMLHttpRequest();
        request.open('GET', '/api/temperature/day', true);
        request.onload = function() {

function getRandomInt(max) {
  return Math.floor(Math.random() * Math.floor(max));
}

            var requestH = new XMLHttpRequest();
            requestH.open('GET', '/api/humidity/day', true);

            var hum = [];
            var recordH = JSON.parse(this.response);
                recordH.forEach(v => {
                   console.log(v.value);
                   hum.push([v.value]);
                });

            var data = new google.visualization.DataTable();
            var rows = [];
            var count = 0;
            data.addColumn('datetime', 'Time');
            data.addColumn('number', 'Temperature');
            data.addColumn('number', 'Humidity');
            var record = JSON.parse(this.response)
            record.forEach(v => {
                console.log(v);
                rows.push([new Date(v.time+'Z'), v.value, parseFloat(hum[count]) + getRandomInt(5)]);
                count = count + 1;
            });
            data.addRows(rows);
            var options = {
              title: 'Temperature',
              curveType: 'function',
              colors: ['#ff623b', '#3bff44'],
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