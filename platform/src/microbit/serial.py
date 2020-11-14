from threading import Thread
from argparse import ArgumentParser
import serial

from src.core.event import Observable
from src.database.entities import Data
from config.microbits import SERIAL_PORT



class Serial(Observable):
    def __init__(self):
        super().__init__()
        parser = ArgumentParser()
        parser.add_argument("--port", type=str, default=SERIAL_PORT)
        args = parser.parse_args()

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
                ret.append(add_data(fields[i], values[i], id[1]))
                pass

        return ret