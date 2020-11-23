# code pour le lien entre l'application android et la platforme
from src.core.event import database_event_manager, user_request_manager
from src.database.entities import Data


def on_data_added_to_database(data: Data):
    # code joué lorsque il y a l'ajout d'une donnée en base
    # c'est à dire quand la microbit nous envoie de nouvelle valeurs

    pass


database_event_manager.register(on_data_added_to_database)


def run_android_link():
    # code qui communique avec l'application android

    # Lors qu'une donnée de l'appli android est reçu
    # pour l'envoyer aux microbits
    # user_request_manager.notify("donnée reçu")
    pass
