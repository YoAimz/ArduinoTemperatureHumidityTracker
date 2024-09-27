let chart;
let stompClient;

function updateLatestReading(data) {
    document.getElementById('temperature').textContent = data.temperature.toFixed(1);
    document.getElementById('humidity').textContent = data.humidity.toFixed(1);
    document.getElementById('timestamp').textContent = new Date(data.timestamp).toLocaleString();
    updateSensorStatus(data.timestamp);
}

function updateSensorStatus(lastTimestamp) {
    const lastComm = new Date(lastTimestamp);
    const now = new Date();
    const diffMinutes = (now - lastComm) / (1000 * 60);

    const statusElement = document.getElementById('sensorStatus');

    if (diffMinutes < 1) {
        statusElement.textContent = 'Online';
        statusElement.className = 'font-bold text-green-600';
    } else if (diffMinutes < 5) {
        statusElement.textContent = 'Warning';
        statusElement.className = 'font-bold text-yellow-600';
    } else {
        statusElement.textContent = 'Offline';
        statusElement.className = 'font-bold text-red-600';
    }
}

function updateHistoryTable(data) {
    const tableBody = document.getElementById('historyTableBody');
    tableBody.innerHTML = ''; // Clear existing rows
    data.slice(-20).reverse().forEach(reading => {
        const row = tableBody.insertRow(-1);
        row.insertCell(0).textContent = new Date(reading.timestamp).toLocaleString();
        row.insertCell(1).textContent = reading.temperature.toFixed(1);
        row.insertCell(2).textContent = reading.humidity.toFixed(1);
    });
}

function updateChart(data) {
    const ctx = document.getElementById('sensorChart').getContext('2d');
    const labels = data.map(reading => new Date(reading.timestamp).toLocaleTimeString());
    const temperatures = data.map(reading => reading.temperature);
    const humidities = data.map(reading => reading.humidity);

    if (chart) {
        chart.data.labels = labels;
        chart.data.datasets[0].data = temperatures;
        chart.data.datasets[1].data = humidities;
        chart.update();
    } else {
        chart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [
                    {
                        label: 'Temperature (°C)',
                        data: temperatures,
                        borderColor: 'rgb(255, 99, 132)',
                        backgroundColor: 'rgba(255, 99, 132, 0.2)',
                        type: 'line',
                        yAxisID: 'y-temperature',
                        tension: 0.1,
                        order: 1
                    },
                    {
                        label: 'Humidity (%)',
                        data: humidities,
                        backgroundColor: 'rgba(54, 162, 235, 0.5)',
                        borderColor: 'rgb(54, 162, 235)',
                        borderWidth: 1,
                        yAxisID: 'y-humidity',
                        order: 2
                    }
                ]
            },
            options: {
                responsive: true,
                scales: {
                    x: {
                        display: true,
                        title: {
                            display: true,
                            text: 'Time'
                        }
                    },
                    'y-temperature': {
                        type: 'linear',
                        display: true,
                        position: 'left',
                        title: {
                            display: true,
                            text: 'Temperature (°C)'
                        },

                        /*,
                        //beginAtZero: false,
                        ticks: {
                            stepSize: 5,
                            callback: function(value){
                                return value + 'C'
                            }
                        }*/
                    },
                    'y-humidity': {
                        type: 'linear',
                        display: true,
                        position: 'right',
                        title: {
                            display: true,
                            text: 'Humidity (%)'
                        },
                        min: 0,
                        max: 100,
                        ticks: {
                            stepSize: 5,
                            callback: function (value) {
                                return value + '%';
                            }
                        },
                        grid: {
                            drawOnChartArea: false
                        }
                    }
                }
            }
        });
    }
}

function fetchReadingHistory() {
    fetch('/api/sensor/history')
        .then(response => response.json())
        .then(data => {
            updateHistoryTable(data);
            updateChart(data);
        })
        .catch(error => console.error('Error fetching history:', error));
}

function connectWebSocket() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('WebSocket connection established');

        stompClient.subscribe('/topic/readings', function (message) {
            const reading = JSON.parse(message.body);
            console.log('New reading received:', reading);
            updateLatestReading(reading);
            fetchReadingHistory();
        });
    }, function (error) {
        console.log('WebSocket connection closed. Trying to reconnect...', error);
        setTimeout(connectWebSocket, 5000);
    });
}

function disconnectWebSocket() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}


document.addEventListener('DOMContentLoaded', function() {
    const latestReading = {
        temperature: parseFloat(document.getElementById('temperature').textContent),
        humidity: parseFloat(document.getElementById('humidity').textContent),
        timestamp: document.getElementById('timestamp').textContent
    };
    updateSensorStatus(new Date(latestReading.timestamp));
    fetchReadingHistory();
    connectWebSocket();
});


window.addEventListener('beforeunload', function() {
    disconnectWebSocket();
});

setInterval(fetchReadingHistory, 30000);