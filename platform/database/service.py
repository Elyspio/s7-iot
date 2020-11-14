from datetime import datetime
from enum import Enum
from json import JSONEncoder, dumps
from typing import Any

from .entities import DataCode as E_DateCode, Data as E_Data, Sensor as E_Sensor
from .tables import Data, DataCode, Sensor
from core.event import database_event_manager

class DataCodeKeys(Enum):
    TEMPERATURE = "TEM"
    LUMINOSITY = "LUM"

    @staticmethod
    def from_str(code: str) -> "self":
        if code == "TEM":
            return DataCodeKeys.TEMPERATURE
        if code == "LUM":
            return DataCodeKeys.LUMINOSITY
        raise NameError(f"Could not find DataCodeKeys with code = {code}")


def get_codes() -> list[E_DateCode]:
    return list(map(E_DateCode.from_db, list(DataCode.select())))


def add_data(code: str, value: str, id_sensor: str) -> E_Data:
    """

    :param code:
    :param value:
    :param id_sensor:
    :return:
    """
    code_db: DataCode = DataCode.get(DataCode.code == code)
    sensor: Sensor = Sensor.get_or_none(Sensor.serial == id_sensor)

    if sensor is None:
        raise NameError(f"No sensor with serial '{id_sensor}'")
    data = Data.create(value=value, code=code_db, sensor=sensor, date=datetime.now().timestamp())

    data = E_Data(data.id, data.value, data.date, code_db, sensor)

    # Notification qu'une nouvelle données est ajouter
    database_event_manager.notify(data)

    return data
    # todo call Jule's function


def get_data(sensor_serial: str) -> list[E_Data]:
    """
    :return: dict(str, str)
    """

    try:
        data: list[Data] = list(Data.select()
                                .join(Sensor, on=(Data.sensor == Sensor.serial))
                                .where(Sensor.serial == sensor_serial))
        return list(map(E_Data.from_db, data))

    except Exception:
        raise NameError(f"could not find a sensor with serial: {sensor_serial}")
        pass


def get_sensors() -> list[E_Sensor]:
    return list(map(E_Sensor.from_db, list(Sensor.select())))


class MyEncoder(JSONEncoder):
    def default(self, o):
        try:
            return o.__dict__
        except AttributeError:
            return o


def to_json(any: Any) -> str:
    return dumps(cls=MyEncoder, obj=any)
