import time
from typing import List, Dict, Type, Union, Tuple

from utils.arduino.AbstractSensorStation import AbstractSensorStation
import asyncio
from bleak import BleakClient, BleakScanner, BleakGATTCharacteristic

from utils.logger.AccessPointLogger import AccessPointLogger

DEVICE_NAME = "Sensorstation G5T2"
SENSOR_TYPES = ["LIGHT", "TEMPERATURE", "AIR_HUMIDITY", "SOIL_HUMIDITY", "AIR_PRESSURE", "AIR_QUALITY"]
LOWER_TIME_BOUND = 10
UPPER_TIME_BOUND = 60


class ArduinoSensorStation(AbstractSensorStation):
    DEVICE_NAME = "Sensorstation G5T2"
    SENSOR_TYPES = ["LIGHT", "TEMPERATURE", "AIR_HUMIDITY", "SOIL_HUMIDITY", "AIR_PRESSURE", "AIR_QUALITY"]

    def __init__(self, logger):
        self.id = None
        self.max_limit = [100 for i in range(6)]
        self.min_limit = [0 for i in range(6)]
        self.client = None
        self.services = None
        self.device_information_service = None
        self.sensor_data_service = None
        self.sensor_limit_service = None
        self.dip_id = None
        self.limit_flags = []
        self.limit_timestamps = []
        self.limit_flags = [0 for i in range(6)]
        self.limit_timestamps = [time.time() for i in range(6)]
        self.limit_report_interval = 10
        self.logger = logger

    def set_min_max(self, sensor_type: str, new_min, new_max):
        i = SENSOR_TYPES.index(sensor_type)
        self.min_limit[i] = new_min
        self.max_limit[i] = new_max

    async def async_init(self, pid: int, id: int):
        self.dip_id = pid
        self.id = id
        localname = "Sensorstation G5T2 " + str(pid)
        print("connecting to " + localname)
        device = await BleakScanner.find_device_by_name(localname)
        if device is None:
            print("error while connecting to " + localname)

        try:
            self.client = await BleakClient(device).__aenter__()
        except Exception as e:
            print(f"Error while creating BleakClient: {e}")
        self.services = self.client.services
        self.device_information_service = self.services.get_service("180A")
        self.sensor_data_service = self.services.get_service("181A")
        self.sensor_limit_service = self.services.get_service("93B5EAD0-0252-460D-9E23-414627CE26E8")
        await self.client.start_notify(
            self.sensor_limit_service.get_characteristic("93B5EAD7-0252-460D-9E23-414627CE26E8"),
            self.gardener_reset_handler)

    async def async_exit(self):
        await self.client.__aexit__(None, None, None)

    async def gardener_reset_handler(self, sender: BleakGATTCharacteristic, data: bytearray):
        print("gardener reset button pressed")
        self.logger.log_sensor_communication("Sensorstation G5T2 " + str(self.get_pid()), "A gardener has pushed the reset button")
        for i in range(6):
            await self.set_sensor_station_warning(self.dip_id, self.SENSOR_TYPES[i], 0)
            self.limit_timestamps[i] = time.time()

    async def get_sensor_data(self):
        characteristics = []
        characteristics.append(self.sensor_data_service.get_characteristic("2A77"))
        characteristics.append(self.sensor_data_service.get_characteristic("2A6E"))
        characteristics.append(self.sensor_data_service.get_characteristic("2A6F"))
        characteristics.append(self.sensor_data_service.get_characteristic("FB170677-D837-4A49-9688-0736D93EAED5"))
        characteristics.append(self.sensor_data_service.get_characteristic("2A6D"))
        characteristics.append(self.sensor_data_service.get_characteristic("2AFB"))
        
        values = []
        for i in range(6):
            value = int.from_bytes(await self.client.read_gatt_char(characteristics[i].uuid), "little")
            await self.manage_limits(SENSOR_TYPES[i], value)
            values.append(value)
        return values

    async def manage_limits(self, sensor_type: str, value):
        i = self.SENSOR_TYPES.index(sensor_type)
        if (value > self.max_limit[i]) or (value < self.min_limit[i]):
            if self.limit_flags[i] == 0:
                self.limit_flags[i] = 1
                self.limit_timestamps[i] = time.time()
            else:
                t = time.time() - self.limit_timestamps[i]
                strength = 0
                if LOWER_TIME_BOUND < t < UPPER_TIME_BOUND:
                    strength = int(((t - LOWER_TIME_BOUND) / (UPPER_TIME_BOUND - LOWER_TIME_BOUND)) * 255)
                elif t > UPPER_TIME_BOUND:
                    strength = 255
                await self.set_sensor_station_warning(self.dip_id, sensor_type, strength)

        elif self.limit_flags[i] > 0:
            self.limit_flags[i] = 0
            await self.set_sensor_station_warning(self.dip_id, sensor_type, 0)

    async def set_sensor_station_warning(self, pidId: int, sensor_type: str, severity: int) -> bool:
        characteristics = []
        characteristics.append(self.sensor_limit_service.get_characteristic("93B5EAD1-0252-460D-9E23-414627CE26E8"))
        characteristics.append(self.sensor_limit_service.get_characteristic("93B5EAD2-0252-460D-9E23-414627CE26E8"))
        characteristics.append(self.sensor_limit_service.get_characteristic("93B5EAD3-0252-460D-9E23-414627CE26E8"))
        characteristics.append(self.sensor_limit_service.get_characteristic("93B5EAD4-0252-460D-9E23-414627CE26E8"))
        characteristics.append(self.sensor_limit_service.get_characteristic("93B5EAD5-0252-460D-9E23-414627CE26E8"))
        characteristics.append(self.sensor_limit_service.get_characteristic("93B5EAD6-0252-460D-9E23-414627CE26E8"))
        
        index = self.SENSOR_TYPES.index(sensor_type)
        await self.client.write_gatt_char(characteristics[index],
                                          severity.to_bytes(1, "little"), True)
        return True

    def get_pid(self):
        return self.dip_id
