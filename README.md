# Temperature & Humidity Dashboard

## Real-time Temperature and Humidity Monitoring

TempHum IoT Dashboard is a comprehensive IoT solution for real-time monitoring of temperature and humidity. This project integrates hardware, backend, and frontend to create a seamless experience of data collection and visualization.

### Key Features:
- 🌡️ Real-time temperature and humidity measurement
- 📊 Dynamic graphical representation of sensor data
- 📡 WebSocket-based real-time communication
- 🗄️ Data storage with MongoDB for historical analysis
- 📱 Responsive web design for access from various devices

### Screenshots:
<img src="https://github.com/user-attachments/assets/49ab53e2-44a0-41da-930b-1ce42781f0b6" alt="Temp Hemp PIC" width="600">

<img src="https://github.com/user-attachments/assets/939f757a-eff6-441e-9d92-dfce79f5a06c" alt="Temp Hemp Arduino Circuit" width="600">


### Tech Stack:
- Arduino (Sensor & Data Collection)
- Spring Boot (Backend)
- MongoDB (Data Storage)
- WebSocket (Real-time Communication)
- Thymeleaf & JavaScript (Frontend)
- Chart.js (Data Visualization)

### Getting Started:
1. Clone the repository
2. Set up Arduino
- Connect DHT11 sensor to Arduino
     - Black wire -> GND (Ground) - and connect to left most pin on the DHT11 sensor.
     - White wire -> 5V - goes to the right most pin on the DHT11 snesor.
     - Red wire -> Digital Pin 2 (on arduino board) - connect to the the second pin from left on the DHT11 sensor. 
- Upload the provided Arduino sketch
3. Set up MongoDB
- Install MongoDB
- Create a database named 'sensor_data', Note: it automatically creates if it dosn't find it. You can skip this step.
4. Configure Spring Boot application
- Update `application.properties` with your MongoDB details
5. Run the Spring Boot application
6. Open a web browser and navigate to `http://localhost:8080`

## Arduino Configuration

- The Arduino sketch is configured to read data from the DHT11 sensor every 2 seconds.
- This frequent update interval is set for demonstration purposes to show real-time capabilities.
- Change the arduino sketch delay to your desired interval update.
