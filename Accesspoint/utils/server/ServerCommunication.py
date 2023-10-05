import json
import threading

from utils.server.RestApi import RestApi
from utils.database.Database import Database

from utils.logger.AccessPointLogger import AccessPointLogger


class ServerCommunication:
    def __init__(self, interval, host, usr, passwd, access_point_id, logger):
        self.interval = interval
        self.host = host
        self.access_point_id = access_point_id
        self.api = RestApi(host, usr, passwd)
        self.usr = usr
        self.logger = logger

    async def start(self):
        print("Starting server communication")
        self.logger.log_server_transmission(self.usr, "Communication established")
        self.transmit_data()
        self.update_access_point()

    def transmit_data(self):
        try:
            print("Transmitting data...")
            with Database() as db:
                db.save_settings_to_database(
                    self.api.get_access_point(self.access_point_id))
                self.interval = db.get_transmission_interval()
                sensorData = db.get_all_sensordata()
            print("length sensordata: " + str(len(sensorData)))
            returned_data = self.api.post_sensor_data(sensorData)
            with Database() as db:
                db.delete_sensor_data()
                print("Data Transmitted")
        except:
            print("an exception occurred in the Server communication")
            self.logger.log_server_transmission(self.usr, "an exception occurred in the Server communication")

        self.timer = threading.Timer(self.interval, self.transmit_data)
        self.timer.start()

    def update_access_point(self):
        try:
            with Database() as db:
                db.save_settings_to_database(self.api.get_access_point(self.access_point_id))
        except:
            print("an exception occurred in the Server communication")

        self.timer_update_access_point = threading.Timer(2,self.update_access_point)
        self.timer_update_access_point.start()

    def stop(self):
        print("Stopping server communication...")
        self.logger.log_server_transmission(self.usr, "Communication lost")
        self.timer.cancel()
        self.timer_update_access_point.cancel()
        print("Server communication stopped.")
