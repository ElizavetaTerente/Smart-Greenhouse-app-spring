import asyncio
import threading
import time

from utils.logger.AccessPointLogger import AccessPointLogger
from utils.arduino.ArduinoSensorStation import ArduinoSensorStation
from utils.Config import Config
from utils.server.ServerCommunication import ServerCommunication
from utils.arduino.SensorStationCommunication import SensorStationCommunication
from utils.arduino.RandomSensorStation import RandomSensorStation
# from utils.arduino.ArduinoCommunication import ArduinoCommunication


def start_configuration():
    config = Config()
    config.start_accesspoint()
    return config.get_config_settings()


async def main():
    host, access_point_id, usr, passwd = start_configuration()
    sensorstation_log_location = "sensorstation_log.json"
    restCommunication_log_location = "restCommunication_log.json"

    server_comm = ServerCommunication(10, host, usr, passwd, access_point_id, AccessPointLogger(restCommunication_log_location))
    arduino_comm = SensorStationCommunication(5, host, usr, passwd, AccessPointLogger(sensorstation_log_location))

    task1 = asyncio.create_task(server_comm.start())
    task2 = asyncio.create_task(arduino_comm.start())

    try:
        while True:
            await asyncio.sleep(1)
    except KeyboardInterrupt:
        print(" Terminating program...")
        await asyncio.gather(task1, task2)
        exit()


if __name__ == "__main__":
    asyncio.run(main())
