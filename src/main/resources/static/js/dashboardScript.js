const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/readings', function (message) {
        updateDashboard(JSON.parse(message.body));
    });
}, function(error) {
    console.error('!STOMP! error:', error);
});

function updateDashboard(data) {
    document.getElementById('temperature').textContent = data.temperature.toFixed(1);
    document.getElementById('humidity').textContent = data.humidity.toFixed(1);
    document.getElementById('timestamp').textContent = new Date(data.timestamp).toLocaleString();

    const historyTable = document.getElementById('historyTable');
    const newRow = historyTable.insertRow(1);
    newRow.insertCell(0).textContent = new Date(data.timestamp).toLocaleString();
    newRow.insertCell(1).textContent = data.temperature.toFixed(1);
    newRow.insertCell(2).textContent = data.humidity.toFixed(1);


}