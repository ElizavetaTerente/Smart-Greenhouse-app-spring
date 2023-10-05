from ast import List, Tuple
import datetime
import random
import time
import uuid

from utils.arduino.AbstractSensorStation import AbstractSensorStation


class RandomSensorStation(AbstractSensorStation):

    def __init__(self):
        self.sensor_types = ["SOIL_HUMIDITY", "TEMPERATURE", "AIR_HUMIDITY", "AIR_PRESSURE", "AIR_QUALITY", "LIGHT"]

    # sensor_types = ["SOIL_HUMIDITY", "TEMPERATURE", "AIR_HUMIDITY", "AIR_PRESSURE", "AIR_QUALITY", "LIGHT"]

    def get_sensor_data(self, ids_and_stations: list[tuple[int, int]]) -> list[dict[str, str, float, int]]:
        data = []
        for pidId, sensorStationId in ids_and_stations:
            for sensor_type in self.sensor_types:
                # generate random value between 0 and 100
                data_value = round(random.uniform(0, 100), 2)
                data.append({
                    "pidId": pidId,
                    "sensorType": sensor_type,
                    "dataTime": time.strftime('%Y-%m-%dT%H:%M:%S'),  # current time
                    "dataValue": data_value,
                    "sensorStationId": sensorStationId
                })
        return data

    def set_sensor_station_warning(self, pidId : int, sensor_type : str, severity : int) -> bool:
        print(f"warning {sensor_type} with severity {severity}")
        return True     # successfully transmitted

    def search_for_new_sensor_station(self) -> int:
        return random.randint(0,100)    # random DIP Value, if it is -1 no sensor_station was found
