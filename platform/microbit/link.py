# code pour le lien entre l'application android et la platforme+
# extract data from uart
from argparse import ArgumentParser
from threading import Thread

import serial

from core.event import Observable
from database.service import add_data
from database.service import get_data, DataCodeKeys
from database.entities import Data


def run_microbit_link():
    get_data(DataCodeKeys.LUMINOSITY)
    get_data(DataCodeKeys.TEMPERATURE)
    s = Serial()
    s.register(on_update=lambda x: print(f"listen: {str(x)}"))

    pass


parser = ArgumentParser()

parser.add_argument("--port", required=True, type=str)
args = parser.parse_args()


class Serial(Observable):
    def __init__(self):
        super().__init__()
        self.serial = serial.Serial(args.port, 115200)
        Thread(target=self.listen).start()

    def listen(self):
        while self.serial.isOpen():
            raw = self.serial.readline()
            print(f"raw {raw}")

            data = raw.decode("utf-8")[0:-2]
            self.create_obj_from_serial(data)
            print(f"Read: {data}")
            self.notify(data)

    def create_obj_from_serial(self, record: str) -> list[Data]:

        [fields, values] = map(lambda x: x.split(","), record.split("|"))

        id = (fields[0], values[0])

        print(fields)
        print(values)

        ret = []

        if id[0] != "ID":
            print("malformed serial input")
        else:
            for i in range(1, len(fields)):
                # ret.append(add_data(fields[i], values[i], id[1]))
                pass

        return ret
