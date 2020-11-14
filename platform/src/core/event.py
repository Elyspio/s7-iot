from typing import Callable, Any


class Observable:

    def __init__(self):
        self._observers = []

    def register(self, on_update: Callable[[Any], None]):
        self._observers.append(on_update)

    def notify(self, *args):
        for observer in self._observers:
            observer(*args)


database_event_manager = Observable()

user_request_manager = Observable()
