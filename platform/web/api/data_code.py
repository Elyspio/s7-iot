from flask import jsonify

from database import service, entities
from ..server import app


@app.route("/api/data_codes/")
def get_codes():
    return service.to_json(service.get_codes())

