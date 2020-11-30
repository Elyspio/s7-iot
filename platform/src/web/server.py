from flask import Flask
from flask_cors import CORS
import os

front_folder = os.path.join(os.path.dirname(__file__), "front", "build")
print("front production folder ", front_folder)
app = Flask(__name__, static_folder=front_folder, static_url_path='/')

# Allow cors on all requests
CORS(app)


@app.route('/')
def index():
    return app.send_static_file('index.html')


@app.route("/api/")
def check_server_presence():
    return ""

def run_web_server():
    app.run(debug=True, use_reloader=False)


# Don't delete these imports
from .api.data import get_data
from .api.data_code import get_codes
from .api.sensors import get_sensors
