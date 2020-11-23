from src.database.tables import initialize_db
from src.web.server import run_web_server
from src.android.link import run_android_link
from src.microbit.link import run_microbit_link

initialize_db()
run_android_link()
run_microbit_link()
run_web_server()
