# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.
from android.link import run_android_link
from database.tables import initialize_db
from microbit.link import run_microbit_link
from web.server import run_web_server

initialize_db()
run_android_link()
# run_microbit_link()
run_web_server()
# See PyCharm help at https://www.jetbrains.com/help/pycharm/
