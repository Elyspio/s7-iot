import os
from datetime import datetime

import peewee as pw

folder = os.path.dirname(__file__)
dbPath = os.path.join(folder, 'database.db')
print(f"Database path: {dbPath}")
db = pw.SqliteDatabase(dbPath)


class DataCode(pw.Model):
    id = pw.IntegerField(primary_key=True, constraints=[pw.SQL("AUTOINCREMENT")])
    label = pw.CharField()
    code = pw.CharField()

    class Meta:
        database = db
        table_name = "data_code"


class Sensor(pw.Model):
    serial = pw.CharField(primary_key=True)
    label = pw.CharField()

    class Meta:
        database = db
        table_name = "sensor"


class Data(pw.Model):
    id = pw.IntegerField(primary_key=True, constraints=[pw.SQL("AUTOINCREMENT")])
    value = pw.CharField()
    date = pw.FloatField()
    code = pw.ForeignKeyField(DataCode, field="id", column_name="type", )
    sensor = pw.ForeignKeyField(Sensor,  field="serial", column_name="sensor")

    class Meta:
        table_name = "data"
        database = db


def initialize_db():
    with db:
        db.create_tables([DataCode, Sensor])
        db.create_tables([Data])
        pass
