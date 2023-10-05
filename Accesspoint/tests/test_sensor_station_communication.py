import unittest
from unittest.mock import MagicMock
from utils.arduino.SensorStationCommunication import SensorStationCommunication
from utils.database.Database import Database

class TestSensorStationCommunication(unittest.TestCase):

    def setUp(self):
        self.sensor_station_mock = MagicMock()
        self.interval = 5
        self.host = "localhost"
        self.usr = "username"
        self.passwd = "password"
        self.ssc = SensorStationCommunication(
            self.sensor_station_mock, self.interval, self.host, self.usr, self.passwd
        )

    def test_start(self):
        self.ssc.retrieveSensorData = MagicMock()
        self.ssc.start()
        self.ssc.retrieveSensorData.assert_called_once()

    def test_stop(self):
        self.ssc.timer = MagicMock()
        self.ssc.stop()
        self.ssc.timer.cancel.assert_called_once()

    def test_search_new_sensor_station(self):
        self.sensor_station_mock.search_for_new_sensor_station.return_value = "New station found"
        self.ssc.api.post_new_sensor_station = MagicMock()
        self.ssc.search_new_sensor_station()
        self.ssc.api.post_new_sensor_station.assert_called_with(
            "New station found")

    def test_retrieveSensorData(self):
        sensor_data = [{"id": 1, "value": 25}]
        enabled_sensor_stations = [{"id": 1, "enabled": True}]
        self.sensor_station_mock.get_sensor_data.return_value = sensor_data

        with Database() as db:
            db.get_enabled_sensor_stations = MagicMock(
                return_value=enabled_sensor_stations)
            db.save_sensor_data = MagicMock()

            self.ssc.retrieveSensorData()

            self.sensor_station_mock.get_sensor_data.assert_called_with(
                enabled_sensor_stations)
            db.save_sensor_data.assert_called_with(sensor_data)


if __name__ == '__main__':
    unittest.main()
