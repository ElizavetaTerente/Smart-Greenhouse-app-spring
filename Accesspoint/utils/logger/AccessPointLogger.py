import datetime
import json
import os

class AccessPointLogger:

    def __init__(self, log_file):
        self.log_file = log_file

        # Create log file if it does not exist
        if not os.path.exists(log_file):
            with open(log_file, "w") as f:
                f.write("[]")

    def _read_log(self):
        with open(self.log_file, "r") as f:
            log_data = json.load(f)
        return log_data

    def _write_log(self, log_data):
        with open(self.log_file, "w") as f:
            json.dump(log_data, f, indent=4, sort_keys=True)

    def _log_event(self, event_type, user, event_info):
        timestamp = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        log_entry = {
            "timestamp": timestamp,
            "event_type": event_type,
            "user": user,
            "event_info": event_info
        }

        log_data = self._read_log()
        log_data.append(log_entry)
        self._write_log(log_data)

    def log_sensor_communication(self, user, event_info):
        self._log_event("sensor_communication", user, event_info)

    def log_server_transmission(self, user, event_info):
        self._log_event("server_transmission", user, event_info)

    def log_connection_issue(self, user, event_info):
        self._log_event("connection_issue", user, event_info)

# Usage example
if __name__ == "__main__":
    logger = AccessPointLogger("accesspoint_log.json")
    logger.log_sensor_communication("user1", {"sensor_id": 1, "data": "example_data"})
    logger.log_server_transmission("user2", {"data": "transmitted_data"})
    logger.log_connection_issue("user3", {"error": "Connection timeout"})
