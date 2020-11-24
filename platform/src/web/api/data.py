from src.database import service
from ..server import app


@app.route("/api/data/<sensor_id>")
def get_data(sensor_id: str):
    return service.to_json(service.get_data(sensor_id))


@app.route("/api/data/last/<sensor_id>/<type_id>")
def get_last_data(sensor_id,type_id: str):
    return service.to_json(service.get_last_data(sensor_id, type_id))