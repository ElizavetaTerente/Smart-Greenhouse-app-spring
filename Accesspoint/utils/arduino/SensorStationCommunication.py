import asyncio
import time
import re

from bleak import BleakScanner
# import threading

from utils.arduino.AbstractSensorStation import AbstractSensorStation
from utils.database.Database import Database
from utils.server.RestApi import RestApi

from utils.arduino.ArduinoSensorStation import ArduinoSensorStation

from utils.logger.AccessPointLogger import AccessPointLogger


async def searching():
    print("searching for new stations")
    devices = await BleakScanner.discover(timeout=10.0, return_adv=True)

    regex = "^Sensorstation G5T2 ([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])$"
    lst = []

    for k, v in devices.items():
        bluetooth_name = v[1].local_name
        if bluetooth_name is not None and re.search(regex, bluetooth_name):
            lst.append(int(v[1].local_name.removeprefix("Sensorstation G5T2 ")))

    print("found stations: {0}".format(lst))
    return lst


class SensorStationCommunication:

    def __init__(self, interval, host, usr, passwd, logger):
        self.sensor_stations = []
        self.interval = interval
        self.api = RestApi(host, usr, passwd)
        self.usr = usr
        self.logger = logger

    async def start(self):
        self.logger.log_sensor_communication(self.usr, "Starting sensor station communication")
        print("Starting sensor station communication")
        await self.retrieve_sensor_stations_data()

    async def stop(self):
        #  await self.sensor_station.async_exit
        print("Stopping sensor station...")
        print("Sensor Station communication stopped.")

    async def connect(self, pid, id):
        self.logger.log_sensor_communication(self.usr, "Trying to connect to Sensorstation with Pid " + str(pid))
        try:
            newStation = ArduinoSensorStation(self.logger)
            await newStation.async_init(pid, id)
            self.sensor_stations.append(newStation)
            self.logger.log_sensor_communication(self.usr, "Successfully connected to Sensorstation with Pid " + str(pid))
        except:
            self.logger.log_sensor_communication(self.usr, "Failed to connect to Sensorstation with Pid " + str(pid))

    async def disconnect(self, sensor_station):
        print("disconnecting from station pid: " + str(sensor_station.get_pid()))
        self.logger.log_sensor_communication(self.usr, "disconnecting from station pid: " + str(sensor_station.get_pid()))
        await sensor_station.async_exit()
        self.sensor_stations.remove(sensor_station)

    async def search_new_sensor_station(self):
        with Database() as db:
            access_point_id = db.get_access_point_id()  # change to env
        found_station_pids = await searching()
        self.logger.log_sensor_communication(self.usr, "Found the following Sensorstations while discovering: " + str(found_station_pids))
        for pid in found_station_pids:
            self.api.post_new_sensor_station(pid, access_point_id)

        access_point = self.api.get_access_point(access_point_id)
        with Database() as db:
            db.save_settings_to_database(access_point)

    # def retrieveSensorData(self):
    #     with Database() as db:
    #         sensor_data = self.sensor_station.get_sensor_data(db.get_enabled_sensor_stations())
    #         db.save_sensor_data(sensor_data)
    #         is_searching_sensorstation = db.is_searching_sensor_station()
    #     if is_searching_sensorstation:
    #         self.search_new_sensor_station()
    #     self.timer = threading.Timer(self.interval, self.retrieveSensorData)
    #     self.timer.start()

    async def retrieve_sensor_stations_data(self):
        while True:
            try:
                with Database() as db:
                    sensor_station_pids = db.get_enabled_sensor_stations()
                    is_searching_sensorstation = db.is_searching_sensor_station()

                # remove removed Sensor stations
                for sensor_station in self.sensor_stations:
                    if sensor_station.get_pid() not in list(map(lambda x: x[0], sensor_station_pids)):
                        await self.disconnect(sensor_station)

                # Connect to new SensorStations
                current_saved_pids = list(map(lambda x: x.get_pid(), self.sensor_stations))
                for sensor_station in sensor_station_pids:
                    if int(sensor_station[0]) not in current_saved_pids:
                        await self.connect(sensor_station[0], sensor_station[1])

                # set Sensor thresholds
                with Database() as db:
                    for sensor_station in self.sensor_stations:
                        for i in range(6):
                            sensor_type = ArduinoSensorStation.SENSOR_TYPES[i]
                            min_max = db.get_min_max_threshold(sensor_station.get_pid(), sensor_type)
                            sensor_station.set_min_max(sensor_type, min_max[0], min_max[1])

                for sensor_station in self.sensor_stations:
                    sensor_datas = await sensor_station.get_sensor_data()
                    current_time = time.strftime('%Y-%m-%dT%H:%M:%S')
                    toSave = []
                    for i in range(6):
                        toSave.append({
                            'sensorType': ArduinoSensorStation.SENSOR_TYPES[i],
                            'dataTime': current_time,
                            'dataValue': float(sensor_datas[i]),
                            'sensorStationId': sensor_station.id
                        })
                    with Database() as db:
                        db.save_sensor_data(toSave)

                with Database() as db:
                    access_point_id = db.get_access_point_id()
                if is_searching_sensorstation:
                    await self.search_new_sensor_station()
                    found_station_pids = await searching()
                    for pid in found_station_pids:
                        self.api.post_new_sensor_station(pid, access_point_id)

            except Exception as e:
                print("an exception occurred in the Sensorstation communication")
                print(e)
            await asyncio.sleep(self.interval)
