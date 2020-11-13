from datetime import datetime
from typing import Any

from .tables import DataCode as T_DataCode, Sensor as T_Sensor


class DataCode:

    @staticmethod
    def from_db(record: Any):
        pass

    def __init__(self, id: int, label: str, code: str):
        self.id = id
        self.label = label
        self.code = code


class Sensor:

    @staticmethod
    def from_db(record: Any):
        pass

    def __init__(self, serial: str, label: str):
        self.serial = serial
        self.label = label


class Data:

    def __init__(self, id: int, value: str, date: datetime, code: DataCode, sensor: Sensor):
        self.id = id
        self.value = value
        self.date = date
        self.sensor = sensor
        self.code = code

