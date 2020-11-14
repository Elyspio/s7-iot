from datetime import datetime

from .tables import DataCode as T_DataCode, Sensor as T_Sensor, Data as T_Data


class DataCode:

    @staticmethod
    def from_db(db: T_DataCode) -> "self":
        return DataCode(db.id, db.label, db.code)

    def __init__(self, id: int, label: str, code: str):
        self.id = id
        self.label = label
        self.code = code


class Sensor:

    def __init__(self, serial: str, label: str):
        self.serial = serial
        self.label = label

    @staticmethod
    def from_db(db: T_Sensor) -> "self":
        return Sensor(db.serial, db.label)


class Data:

    @staticmethod
    def from_db(db: T_Data) -> "self":
        return Data(db.id, db.value, db.date, DataCode.from_db(db.code), Sensor.from_db(db.sensor))

    def __init__(self, id: int, value: str, date: datetime, code: DataCode, sensor: Sensor):
        self.id = id
        self.value = value
        self.date = date
        self.sensor = sensor
        self.code = code
