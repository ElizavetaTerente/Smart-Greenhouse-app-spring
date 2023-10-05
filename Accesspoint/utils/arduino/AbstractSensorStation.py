from abc import ABC, abstractmethod
from ast import List


class AbstractSensorStation(ABC):

    @abstractmethod
    def get_sensor_data(self, ids_and_stations: list[tuple[int, int]]) -> list[dict[str, str, float, int]]:
        pass

    @abstractmethod
    def set_sensor_station_warning(self, pidId : int, sensor_type : str, severity : int) -> bool:
        pass


    async def async_init(self):
        pass