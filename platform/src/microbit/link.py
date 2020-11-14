# code pour le lien entre l'application android et la platforme+
# extract data from uart
from argparse import ArgumentParser

from src.microbit.serial import Serial


def run_microbit_link():
    s = Serial()
    s.register(on_update=lambda x: print(f"listen: {str(x)}"))

    pass





