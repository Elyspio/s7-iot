from src.database import service
from ..server import app


@app.route("/api/sensors")
def get_sensors():
    return service.to_json(service.get_sensors())
