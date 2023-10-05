import yaml
import os
import time

from utils.server.RestApi import RestApi
from utils.database.Database import Database

current_dir = os.path.dirname(os.path.abspath(__file__))
config_path = os.path.join(current_dir, "../config.yaml")


class Config:
        
    CONFIG_FILE = "config.yaml"
    current_dir = os.path.dirname(os.path.abspath(__file__))
    config_path = os.path.join(current_dir, "../config.yaml")
        

    def get_config_settings(self):
        return self.address, self.accesspoint_id, self.usr, self.passwd
    
    def load_config(self):
        """Load configuration data from YAML file"""
        try: 
            with open(config_path) as f:
                config_data = yaml.safe_load(f)
                self.address = config_data.get("address", None)
                self.location_name = config_data.get("location_name", None)
                self.accesspoint_id = config_data.get("accesspoint_id", None)
                self.usr = config_data.get("username",None)
                self.passwd = config_data.get("password", None)
        except:
            print("Before executing main.py, please run ./configure to define all necessary information.")
            exit()

    def config_finished(self):
        return not self.address == None or self.location_name == None or self.usr == None or self.passwd == None
    
    def save_accesspoint_id_to_yaml(self,accesspoint_id):
        """Add or change accesspoint_id in configuration"""
        try:
            with open(self.CONFIG_FILE) as f:
                config_data = yaml.safe_load(f)
            config_data["accesspoint_id"] = accesspoint_id
            with open(self.CONFIG_FILE,'w') as f:
                yaml.dump(config_data, f)
        except:
            print("Couldn't write to yaml file.")
            exit()
        self.load_config()

    def start_accesspoint(self):
        self.load_config()
        
        if not self.config_finished():
            print("Please define all necessary information in the config.yaml file. For this, use the bash configure script.")
            exit()
        
        self.api = RestApi(self.address, self.usr, self.passwd)

        start_time = time.time()
        while time.time() - start_time < 60:  # try for 1 minute
            if not self.api.test_connection():
                print("Check connection to Server!")
                time.sleep(2)  # sleep for 1 second before trying again
            else:
                break
        else:  # no break = no successful connection in the given time
            print(f"Failed to establish a connection with the address {self.address} and user {self.usr} after 1 minute!")
            print(f"No connection with the address {self.address} and user {self.usr} possible!")
            exit()

        with Database() as db:
            if self.accesspoint_id == None:
                print("First initialization with Web Server...")
                db.save_settings_to_database(self.api.post_create_access_point(self.location_name))
                self.save_accesspoint_id_to_yaml(db.get_access_point_id())
            else:
                print("Loading Accesspoint Data...")
                db.save_settings_to_database(self.api.get_access_point(self.accesspoint_id))
            
            enabled, accepted = db.is_enabled()
            while enabled == False or accepted == False:
                print("Accesspoint isn't enabled or accepted yet.")
                time.sleep(5)
                db.save_settings_to_database(self.api.get_access_point(self.accesspoint_id))
                enabled, accepted = db.is_enabled()
                
                
                
