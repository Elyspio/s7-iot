from src.database import service
from ..server import app
from src.core.event import user_request_manager
from flask import request

@app.route("/api/sensors")
def get_sensors():
    return service.to_json(service.get_sensors())

@app.route("/api/sensors/order", methods=["POST"])
def sensor_order():
    print(request.form)
    user_request_manager.notify(request.form)
    return ''