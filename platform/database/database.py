from datetime import datetime
from enum import Enum
from typing import Dict

from .entities import DataCode as E_DateCode, Data as E_Data
from .tables import Data, DataCode, Sensor


class DataCodeKeys(Enum):
    TEMPERATURE = "TEM"
    LUMINOSITY = "LUM"


def get_code(type: DataCodeKeys) -> E_DateCode:
    t: DataCode = DataCode.get(DataCode.code == type)
    return E_DateCode(t.id, t.label, t.code)


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

    return E_Data(data.id, data.value, data.date, code_db, sensor)

    # todo call Jule's function


def get_data(type: DataCodeKeys) -> list[E_Data]:
    """
    :return: dict(str, str)
    """

    try:
        t: DataCode = DataCode.get(DataCode.code == type.value)

        data: list[Data] = list(Data.select().where(Data.code == t.id))
        print(data)

        ret = []
        for d in data:
            ret.append(E_Data(d.id, d.value, d.date, d.code, d.sensor))

        return ret
    except Exception:
        raise NameError(f"could not find a data_type that has name: {type}")
        pass
