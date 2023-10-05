-- $2a$10$DWHhsRtjYX33SSKvNcQt0.fKatbz90J4wPc7w2w0S7h5q2chnA3.2 -> passwd
INSERT INTO USERX (FIRSTNAME,LASTNAME, USERNAME, PASSWORD, ENABLED, CREATE_DATE,EMAIL,PHONE) VALUES('Amir', 'Dzelalagic','amir', '$2a$10$DWHhsRtjYX33SSKvNcQt0.fKatbz90J4wPc7w2w0S7h5q2chnA3.2', true, '2023-04-01T18:19:26.492745400','amir@google.com','+436701111111');
INSERT INTO USERX_USER_ROLE (USERX_USERNAME, ROLES) VALUES ('amir', 'ADMIN');
INSERT INTO USERX (FIRSTNAME,LASTNAME, USERNAME, PASSWORD, ENABLED, CREATE_DATE,EMAIL,PHONE) VALUES('Daniel', 'Stoffler','daniel', '$2a$10$DWHhsRtjYX33SSKvNcQt0.fKatbz90J4wPc7w2w0S7h5q2chnA3.2', true, '2023-04-01T18:19:26.492745400','daniel@google.com','+436702222222');
INSERT INTO USERX_USER_ROLE (USERX_USERNAME, ROLES) VALUES ('daniel', 'GARDENER');
INSERT INTO USERX (FIRSTNAME,LASTNAME, USERNAME, PASSWORD, ENABLED, CREATE_DATE,EMAIL,PHONE) VALUES('Elizaveta', 'Terente','elizaveta', '$2a$10$DWHhsRtjYX33SSKvNcQt0.fKatbz90J4wPc7w2w0S7h5q2chnA3.2', true, '2023-04-01T18:19:26.492745400','eli@google.com','+436703333333');
INSERT INTO USERX_USER_ROLE (USERX_USERNAME, ROLES) VALUES ('elizaveta', 'USER');
INSERT INTO USERX (FIRSTNAME,LASTNAME, USERNAME, PASSWORD, ENABLED, CREATE_DATE,EMAIL,PHONE) VALUES('Kristina', 'Alexandrova','kristina', '$2a$10$DWHhsRtjYX33SSKvNcQt0.fKatbz90J4wPc7w2w0S7h5q2chnA3.2', true, '2023-04-01T18:19:26.492745400','kris@google.com','+436704444444');
INSERT INTO USERX_USER_ROLE (USERX_USERNAME, ROLES) VALUES ('kristina', 'GARDENER');
INSERT INTO USERX (FIRSTNAME,LASTNAME, USERNAME, PASSWORD, ENABLED, CREATE_DATE,EMAIL,PHONE) VALUES('Lukas', 'Geisler','lukas', '$2a$10$DWHhsRtjYX33SSKvNcQt0.fKatbz90J4wPc7w2w0S7h5q2chnA3.2', true, '2023-04-01T18:19:26.492745400','lukas@google.com','+436705555555');
INSERT INTO USERX_USER_ROLE (USERX_USERNAME, ROLES) VALUES ('lukas', 'USER');
INSERT INTO USERX (FIRSTNAME,LASTNAME, USERNAME, PASSWORD, ENABLED, CREATE_DATE,EMAIL,PHONE) VALUES('Name', 'Surname','admin', '$2a$10$DWHhsRtjYX33SSKvNcQt0.fKatbz90J4wPc7w2w0S7h5q2chnA3.2', true, '2023-04-01T18:19:26.492745400','name@google.com','+436706666666');
INSERT INTO USERX_USER_ROLE (USERX_USERNAME, ROLES) VALUES ('admin', 'ADMIN');


INSERT INTO `accesspoint` (`accesspoint_id`, `accessible`, `create_date`, `enabled`, `location_name`, `searching_sensor_station`, `transmission_interval`, `update_date`, `create_user_username`, `update_user_username`, `accepted`) VALUES ('1', b'1', NULL, b'1', 'west wing', b'0', '5', NULL, NULL, NULL, b'0');
INSERT INTO `accesspoint` (`accesspoint_id`, `accessible`, `create_date`, `enabled`, `location_name`, `searching_sensor_station`, `transmission_interval`, `update_date`, `create_user_username`, `update_user_username`, `accepted`) VALUES ('2', b'1', NULL, b'1', 'east wing', b'0', '10', NULL, NULL, NULL, b'0');
INSERT INTO `accesspoint` (`accesspoint_id`, `accessible`, `create_date`, `enabled`, `location_name`, `searching_sensor_station`, `transmission_interval`, `update_date`, `create_user_username`, `update_user_username`, `accepted`) VALUES ('3', b'0', NULL, b'1', 'north wing', b'0', '15', NULL, NULL, NULL, b'0');
INSERT INTO `accesspoint` (`accesspoint_id`, `accessible`, `create_date`, `enabled`, `location_name`, `searching_sensor_station`, `transmission_interval`, `update_date`, `create_user_username`, `update_user_username`, `accepted`) VALUES ('30', b'0', NULL, b'1', 'north wing', b'0', '15', NULL, NULL, NULL, b'0');

INSERT INTO `sensor_station` (`sensor_station_id`, `accessible`, `create_date`, `enabled`, `sensor_station_dip_id`, `sensor_station_name`, `universally_unique_identifier`, `update_date`, `accesspoint_accesspoint_id`, `create_user_username`, `update_user_username`, `accesspoint_id`, `accepted`) VALUES ('1', b'1', NULL, b'1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,b'0');
INSERT INTO `sensor_station` (`sensor_station_id`, `accessible`, `create_date`, `enabled`, `sensor_station_dip_id`, `sensor_station_name`, `universally_unique_identifier`, `update_date`, `accesspoint_accesspoint_id`, `create_user_username`, `update_user_username`, `accesspoint_id`, `accepted`) VALUES ('2', b'1', NULL, b'1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,b'0');
INSERT INTO `sensor_station` (`sensor_station_id`, `accessible`, `create_date`, `enabled`, `sensor_station_dip_id`, `sensor_station_name`, `universally_unique_identifier`, `update_date`, `accesspoint_accesspoint_id`, `create_user_username`, `update_user_username`, `accesspoint_id`, `accepted`) VALUES ('3', b'0', NULL, b'1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,b'0');

INSERT INTO `sensor_limit` (`sensor_limit_id`, `overrun_interval`, `sensor_type`, `threshold_max`, `threshold_min`, `sensorstation_id`) VALUES ('9991', '10', '1', '5', '10', NULL);
INSERT INTO `sensor_limit` (`sensor_limit_id`, `overrun_interval`, `sensor_type`, `threshold_max`, `threshold_min`, `sensorstation_id`) VALUES ('9992', '10', '1', '5', '10', NULL);
INSERT INTO `sensor_limit` (`sensor_limit_id`, `overrun_interval`, `sensor_type`, `threshold_max`, `threshold_min`, `sensorstation_id`) VALUES ('9993', '10', '1', '5', '10', NULL);
