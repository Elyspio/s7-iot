from src.database import service
from ..server import app


@app.route("/api/data_codes/")
def get_codes():
    return service.to_json(service.get_codes())
