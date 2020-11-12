from enum import Enum


class DataType(Enum):
    TEMPERATURE = 1
    LUMINOSITY = 2


"""
Type de donnée en base pour stocker une information de température / niveau de luminosité
"""
class Data:

    def __init__(self, type: DataType, value: any) -> None:
        super().__init__()
        self.type = type
        self.value = value
