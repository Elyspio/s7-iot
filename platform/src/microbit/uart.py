from argparse import ArgumentParser
from threading import Thread

from serial import Serial

from config.microbits import SERIAL_PORT
from src.core.event import Observable


class Uart(Observable):
    def __init__(self):
        super().__init__()
        parser = ArgumentParser()
        parser.add_argument("--port", type=str, default=SERIAL_PORT)
        args = parser.parse_args()

        self.serial = Serial(args.port, 115200)
        Thread(target=self.listen).start()

    def listen(self):
        while self.serial.isOpen():
            raw = self.serial.readline()
            data = raw.decode("utf-8")[0:-2]
            self.notify(data)

    def write_line(self, data: str):

        print("Write on serial " + data )
        self.serial.write(f"{data}#".encode())
