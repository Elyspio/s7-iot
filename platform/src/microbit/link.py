# code pour le lien entre la microbit ratachée à la platforme et cette derniere

from src.core.event import user_request_manager
from src.database.entities import Data
from src.database.service import add_data
from src.microbit.uart import Uart


def on_user_request(val):
    """
    Function called when user trigger an input on android app
    :param val:
    :return:
    """
    pass


def create_obj_from_serial(record: str) -> list[Data]:
    [fields, values] = map(lambda x: x.split(","), record.split("|"))

    id = (fields[0], values[0])

    print(fields)
    print(values)

    ret = []

    if id[0] != "ID":
        print("malformed serial input")
    else:
        for i in range(1, len(fields)):
            ret.append(add_data(fields[i], values[i], id[1]))
            pass

    return ret


def run_microbit_link():
    s = Uart()
    s.register(create_obj_from_serial)
    user_request_manager.register(on_user_request)
    pass
