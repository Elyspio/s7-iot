from flask import Flask
from flask_cors import CORS

app = Flask(__name__)

# Allow cors on all requests
CORS(app)


@app.route('/api/')
def hello_world():
    return 'Hello, World!'


def run_web_server():
    app.run(debug=True, use_reloader=False)

# Don't delete these imports
from .api.data import get_data
from .api.data_code import get_codes
from .api.sensors import get_sensors