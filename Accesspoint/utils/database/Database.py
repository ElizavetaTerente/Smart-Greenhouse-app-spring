from typing import List, Tuple
import sqlite3
import uuid
import json
from datetime import datetime


class Database:

    # def __init__(self):
    #     sqlite3.register_adapter(uuid.UUID, lambda u: str(u))
    #     sqlite3.register_converter('GUID', lambda s: uuid.UUID(s.decode()))
    #     self.con = sqlite3.connect(
    #         'data/accesspoint.db', detect_types=sqlite3.PARSE_DECLTYPES, timeout=10)

    def __enter__(self):
        sqlite3.register_adapter(uuid.UUID, lambda u: str(u))
        sqlite3.register_converter('GUID', lambda s: uuid.UUID(s.decode()))
        self.con = sqlite3.connect(
            'data/accesspoint.db', detect_types=sqlite3.PARSE_DECLTYPES, timeout=10)
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.con.close()

    def get_db_cursor(self):
        cursor = self.con.cursor()
        return cursor

    def save_sensor_data(self, data):
        c = self.get_db_cursor()

        c.execute('''CREATE TABLE IF NOT EXISTS sensorData 
                (sensorType TEXT,
                 dataTime TIME, 
                 dataValue REAL, 
                 sensorStationId INTEGER);''')
        c.executemany(
            "INSERT INTO sensorData VALUES  (:sensorType, :dataTime, :dataValue, :sensorStationId)", data)
        c.connection.commit()

    def delete_sensor_data(self, data):
        c = self.get_db_cursor()
        sql = '''
              DELETE FROM sensorData
              WHERE dataTime = ? AND sensorStationId = ? AND sensorType = ?
              '''

        # Execute the SQL statement for each tuple in the data_list
        for d in data:
            c.execute(sql, (d['dataTime'], d['sensorStationId'], d['sensorType']))
        c.connection.commit()

    def delete_sensor_data(self):
        c = self.get_db_cursor()
        c.execute('''DELETE FROM sensorData''')
        c.connection.commit()
    
    # def save_settings_to_database(self, data: dict):
    #     # get db cursor
    #     c = self.get_db_cursor()

    #     # Create table if it doesn't exist
    #     c.execute('''CREATE TABLE IF NOT EXISTS accesspoint 
    #             (id INTEGER default 1, 
    #              accesspointId INTEGER,
    #              locationName TEXT, 
    #              enabled INTEGER, 
    #              accepted INTEGER,
    #              searchingSensorStation INTEGER,
    #              transmissionInterval Time,
    #              CONSTRAINT UQ_Configuration_ID UNIQUE (id));''')

    #     c.execute('''CREATE TABLE IF NOT EXISTS sensorStations
    #              (sensorStationId INTEGER UNIQUE, sensorStationDipId INTEGER UNIQUE, sensorStationName TEXT, enabled INTEGER, accepted INTEGER, accesspointId INTEGER)''')

    #     c.execute('''CREATE TABLE IF NOT EXISTS sensorlimits
    #              (sensorLimitId INTEGER UNIQUE, sensorType TEXT, overrunInterval Time, thresholdMin REAL, thresholdMax REAL, sensorStationId INTEGER)''')

    #     # Insert data into tables
    #     accesspoint = (1, data['accesspointId'], data['locationName'], int(data['enabled']), int(data['accepted']), int(data['searchingSensorStation']), data['transmissionInterval'])

    #     c.execute(
    #         "REPLACE INTO accesspoint (id, accesspointId, locationName, enabled, accepted, searchingSensorStation, transmissionInterval) VALUES(?,?,?,?,?,?,?)", accesspoint)

    #     if (data['sensorStations'] == None):
    #         c.connection.commit()
    #         print("No sensor stations defined!")
    #         return True

    #     for sensor_station in data['sensorStations']:
    #         sensorstation_settings = (sensor_station['sensorStationId'], sensor_station['sensorStationDipId'],
    #                                   sensor_station['sensorStationName'], int(sensor_station['enabled']), int(sensor_station['accepted']), data['accesspointId'])
    #         c.execute("REPLACE INTO sensorStations VALUES (?, ?, ?, ?, ?, ?)",
    #                   sensorstation_settings)

    #         if (sensor_station['sensorLimits'] == None):
    #             c.connection.commit()
    #             print("No sensor station limits defined!")
    #             return True

    #         for sensorlimit in sensor_station['sensorLimits']:
    #             sensorlimit_data = (sensorlimit['sensorLimitId'], sensorlimit['sensorType'], sensorlimit['overrunInterval'],
    #                                 sensorlimit['thresholdMin'], sensorlimit['thresholdMax'], sensor_station['sensorStationId'])
    #             c.execute(
    #                 "REPLACE INTO sensorlimits VALUES (?, ?, ?, ?, ?, ?)", sensorlimit_data)

    #     c.connection.commit()
    #     return True

    def save_settings_to_database(self, data: dict):
        # get db cursor
        c = self.get_db_cursor()
    
        # Create table if it doesn't exist
        c.execute('''CREATE TABLE IF NOT EXISTS accesspoint 
                (id INTEGER default 1, 
                 accesspointId INTEGER,
                 locationName TEXT, 
                 enabled INTEGER, 
                 accepted INTEGER,
                 searchingSensorStation INTEGER,
                 transmissionInterval Time,
                 CONSTRAINT UQ_Configuration_ID UNIQUE (id));''')
    
        c.execute('''CREATE TABLE IF NOT EXISTS sensorStations
                 (sensorStationId INTEGER UNIQUE, sensorStationDipId INTEGER UNIQUE, sensorStationName TEXT, enabled INTEGER, accepted INTEGER, accesspointId INTEGER)''')
    
        c.execute('''CREATE TABLE IF NOT EXISTS sensorlimits
                 (sensorLimitId INTEGER UNIQUE, sensorType TEXT, overrunInterval Time, thresholdMin REAL, thresholdMax REAL, sensorStationId INTEGER)''')
    
        c.execute('''CREATE TABLE IF NOT EXISTS sensorData 
                (sensorType TEXT,
                 dataTime TIME, 
                 dataValue REAL, 
                 sensorStationId INTEGER);''')
    
        # Delete all sensor stations
        c.execute("DELETE FROM sensorStations")
        c.execute("DELETE FROM sensorLimits")
    
        # Insert data into tables
        accesspoint = (1, data['accesspointId'], data['locationName'], int(data['enabled']), int(data['accepted']), int(data['searchingSensorStation']), data['transmissionInterval'])
    
        c.execute(
            "REPLACE INTO accesspoint (id, accesspointId, locationName, enabled, accepted, searchingSensorStation, transmissionInterval) VALUES(?,?,?,?,?,?,?)", accesspoint)
    
        if (data['sensorStations'] == None):
            c.connection.commit()
            print("No sensor stations defined!")
            return True
    
        for sensor_station in data['sensorStations']:
            sensorstation_settings = (sensor_station['sensorStationId'], sensor_station['sensorStationDipId'],
                                      sensor_station['sensorStationName'], int(sensor_station['enabled']), int(sensor_station['accepted']), data['accesspointId'])
            c.execute("REPLACE INTO sensorStations VALUES (?, ?, ?, ?, ?, ?)",
                      sensorstation_settings)
    
            if (sensor_station['sensorLimits'] == None):
                c.connection.commit()
                print("No sensor station limits defined!")
                return True
    
            for sensorlimit in sensor_station['sensorLimits']:
                sensorlimit_data = (sensorlimit['sensorLimitId'], sensorlimit['sensorType'], sensorlimit['overrunInterval'],
                                    sensorlimit['thresholdMin'], sensorlimit['thresholdMax'], sensor_station['sensorStationId'])
                c.execute(
                    "REPLACE INTO sensorlimits VALUES (?, ?, ?, ?, ?, ?)", sensorlimit_data)
    
        # Delete SensorData where there is no sensorStationId saved anymore
        # c.execute("DELETE FROM sensorData WHERE sensorStationId NOT IN (SELECT sensorStationId FROM sensorStations)")
    
        c.connection.commit()
        return True


    def get_access_point_id(self):
        c = self.get_db_cursor()
        c.execute('''SELECT accesspointId FROM accesspoint''')
        result = c.fetchone()
        return result[0]

    def is_enabled(self):
        c = self.get_db_cursor()
        c.execute('''SELECT enabled, accepted FROM accesspoint''')
        result = c.fetchone()

        return result[0], result[1]

    def is_searching_sensor_station(self):
        c = self.get_db_cursor()
        c.execute('''SELECT searchingSensorStation FROM accesspoint''')
        result = c.fetchone()
        return result[0]

    def get_transmission_interval(self):
        c = self.get_db_cursor()
        c.execute('''SELECT transmissionInterval FROM accesspoint''')
        result = c.fetchone()
        interval_string = result[0]
        interval_format = "%H:%M:%S"
        time_object = datetime.strptime(interval_string, interval_format)
        total_seconds = time_object.hour * 3600 + time_object.minute * 60 + time_object.second
        return total_seconds

    def get_all_sensordata(self):
        c = self.get_db_cursor()
        result = c.execute("""SELECT * FROM sensorData""")
        return result.fetchall()

    def get_sensordata(self) -> List[Tuple[str, str, float, int]]:
        c = self.get_db_cursor()
        result = c.execute("""WITH enabled_stations AS (
            SELECT sensorStationId
            FROM sensorStations
            WHERE enabled = 1)

            SELECT sd.sensorType, sd.dataTime, sd.dataValue, sd.sensorStationId
            FROM sensorData sd
            JOIN enabled_stations es ON sd.sensorStationId = es.sensorStationId;""")
        return result.fetchall()

    def get_last_interval(self, data_type, id_dip_sensorstation, time_in_hours):
        c = self.get_db_cursor()
        result = c.execute("""SELECT * FROM sensorData
                                WHERE dateTimeUtc >= datetime('now', '-:time_in_hours hours') 
                                AND dateTimeUtc <= datetime('now')
                                AND type = :data_type
                                AND id_dip_sensorstation = :id_dip_sensorstation
                                """,
                           {'data_type': data_type, 'time_in_hours': time_in_hours, 'id_dip_sensorstation': id_dip_sensorstation})
        return result.fetchall()

    def get_enabled_sensor_stations(self):
        c = self.get_db_cursor()
        result = c.execute("""SELECT sensorStationDipId, sensorStationId
                              FROM sensorStations
                              WHERE enabled = 1;""")
        return result.fetchall()
    
    # def get_min_max_threshold(self, pid:int, sensor_type: str):
    #     c = self.get_db_cursor()
    #     c.execute('''SELECT sensorStationId
    #                     FROM sensorStations
    #                     WHERE :sensorStationDipId = :pid''')
    #     sensor_station_id = c.fetchone()[0]
    #     c.execute('''SELECT thresholdMin, thresholdMax 
    #                 FROM sensorlimits
    #                 WHERE sensorStationId = :sensor_station_id''')
    #     result = c.fetchone()

    #     return result[0], result[1]

    def get_min_max_threshold(self, pid:int, sensor_type: str):
        c = self.get_db_cursor()
        c.execute('''SELECT sensorStationId
                    FROM sensorStations
                    WHERE sensorStationDipId = ?''', (pid,))
        sensor_station_id = c.fetchone()[0]
        c.execute('''SELECT thresholdMin, thresholdMax 
                    FROM sensorlimits
                    WHERE sensorStationId = ? AND sensorType = ?''', (sensor_station_id, sensor_type,))
        result = c.fetchone()
    
        return result[0], result[1]

# def get_max(cursor, measurement_type, threshold):
#     """
#     Return the maximum measurement for the given measurement_type since the given threshold.
#     :param cursor: database cursor
#     :param measurement_type: the type of measurement
#     :param threshold: timestamp to consider measurements since for maximum
#     :return: list of maximum measurements
#     """
#     result = cursor.execute("""SELECT *
#                                 FROM sensordata
#                                 WHERE value = (
#                                     SELECT MAX(value)
#                                     FROM sensordata
#                                     WHERE dateTimeUtc > :from
#                                         AND type = :measurement_type
#                                 )
#                                 """,
#                             {'measurement_type': measurement_type, 'from': threshold})
#     return result.fetchall()


# def get_min(cursor, measurement_type, threshold):
#     """
#     Return the minium measurement for the given measurement_type since the given threshold.
#     :param cursor: database cursor
#     :param measurement_type: the type of measurement
#     :param threshold: timestamp to consider sensordata since for minimum
#     :return: list of minium sensordata
#     """
#     result = cursor.execute("""SELECT *
#                                 FROM sensordata
#                                 WHERE value = (
#                                     SELECT MIN(value)
#                                     FROM sensordata
#                                     WHERE dateTimeUtc > :from
#                                         AND type = :measurement_type
#                                 )
#                                 """,
#                             {'measurement_type': measurement_type, 'from': threshold})
#     return result.fetchall()


# def get_avg(cursor, measurement_type, threshold):
#     """
#     Return the average measurement for the given measurement_type since threshold
#     :param cursor: database cursor
#     :param measurement_type: the type of measurement
#     :param threshold: timestamp to average sensordata since noc
#     :return: the average measurement
#     """
#     result = cursor.execute("""SELECT AVG(value)
#                                 FROM sensordata
#                                 WHERE dateTimeUtc > :from
#                                 AND type = :measurement_type
#                         """,
#                             {'measurement_type': measurement_type, 'from': threshold})
#     return result.fetchone()[0]
