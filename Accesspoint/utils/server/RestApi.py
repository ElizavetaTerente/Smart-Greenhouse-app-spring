import json
import requests
from requests.auth import HTTPBasicAuth
from typing import List


class RestApi:
    def __init__(self, host, usr, passwd):
        self.host = host
        self.usr = usr
        self.passwd = passwd
        self.auth_header = self.prepare_auth_headers()

    def prepare_auth_headers(self):
        # return HTTPBasicAuth('amir', 'passwd')
        return HTTPBasicAuth(self.usr, self.passwd)

    # def post_sensor_data(self, SensorData: dict) -> dict:
    #     """
    #     Post the given SensorData.
    #     :param SensorData: The SensorData to post as a dictionary
    #     :return: the inserted dictionary
    #     """

    #     resp = requests.post(f'{self.host}/api/SensorData',
    #                          json=SensorData,
    #                          auth=self.auth_header)
    #     # return the deserialized SensorData object here
    #     return resp.json()

    # List[dict]:
    def post_sensor_data(self, SensorData: List[dict], batch_size: int = 100) -> int:
        """
        Post the given SensorData in batches.
        :param SensorData: The SensorData to post as a list of dictionaries
        :param batch_size: The size of each batch (default is 100)
        :return: A list of the inserted SensorData as dictionaries
        """
        sensor_data_list = []
        for sensor_type, data_time, data_value, sensor_station_id in SensorData:
            sensor_data = {
                "sensorType": sensor_type,
                "dataTime": data_time,
                "dataValue": data_value,
                "sensorStationId": sensor_station_id
            }
            sensor_data_list.append(sensor_data)
        resp = requests.post(f'{self.host}/api/SensorDataList',
                             json=sensor_data_list,
                             auth=self.auth_header)
        if resp.status_code != 200:
            print(
                f"Request failed with status {resp.status_code}: {resp.reason}")
            return 0
        else:
            return resp.json()

    def post_create_access_point(self, location_name) -> dict:
        """
        Post the given access_point.
        :param access_point: The access_point to post as a dictionary
        :return: the inserted dictionary
        """
        access_point = {"locationName": location_name}
        resp = requests.post(f'{self.host}/api/AccessPoint',
                             json=access_point,
                             auth=self.auth_header)
        # return the deserialized access_point object here
        return resp.json()

    def post_new_sensor_station(self, pidId: int, access_point_id: int):
        sensor_station = {"sensorStationDipId": pidId}
        resp = requests.post(f'{self.host}/api/NewSensorStation/{access_point_id}',
                             json=sensor_station,
                             auth=self.auth_header)
        return resp.status_code

    def get_access_point(self, id):
        resp = requests.get(f'{self.host}/api/AccessPoint/{id}',
                            auth=self.auth_header)
        return resp.json()

    def test_connection(self) -> bool:
        """
        Check if a connection to the server is possible.
        :return: True if connection is successful, False otherwise
        """
        try:
            resp = requests.get(
                f'{self.host}/api/HealthCheck', auth=self.auth_header)
            if resp.status_code == 200:
                return True
            else:
                return False
        except requests.exceptions.RequestException as e:
            print(f"Connection error: {e}")
            return False
