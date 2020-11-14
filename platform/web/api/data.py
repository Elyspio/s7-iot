from flask import jsonify

from database import service
from ..server import app


@app.route("/api/data/<sensor_id>")
def get_data(sensor_id: str):
    return service.to_json(service.get_data(sensor_id))
