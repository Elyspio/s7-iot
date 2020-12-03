# code pour le lien entre la microbit ratachée à la platforme et cette derniere

from src.core.event import user_request_manager
from src.database.entities import Data
from src.database.service import add_data, DataCodeKeys
from src.microbit.uart import Uart

s = Uart()


def on_user_request(val: {"sensor_id": str, "order": list[str]}):
    """
    Function called when user trigger an input on android app
    :param val:
    :return:
    """
    print("user request", val)
    s.write_line(f"{val['sensor_id']};{''.join(val['order'])}")
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

            try:
                ret.append(add_data(DataCodeKeys.from_str(fields[i]), values[i], id[1]))
            except:
                print("Ignored: ", record)
                pass

    return ret


def run_microbit_link():
    s.register(create_obj_from_serial)
    user_request_manager.register(on_user_request)
    pass
