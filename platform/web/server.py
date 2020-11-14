from flask import Flask
from flask_cors import CORS
import os

folder = os.path.dirname(__file__)
front_folder = os.path.join(folder, "front", "build")
print("front located at ", front_folder)
app = Flask(__name__, static_folder=front_folder, static_url_path='/')

# Allow cors on all requests
CORS(app)


@app.route('/')
def index():
    return app.send_static_file('index.html')

def run_web_server():
    app.run(debug=True, use_reloader=False)

# Don't delete these imports
from .api.data import get_data
from .api.data_code import get_codes
from .api.sensors import get_sensors