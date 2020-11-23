import {Api} from "./api";
import {apiEndpoint} from "../config/sensor";


export type Sensor = {
    serial: string,
    label: string
}


type RawData = {
    "id": number
    "value": string
    "date": number
    "sensor": Sensor
    "code": DataCode
}

export type Data = Omit<RawData, "date"> & {
    date: Date
}

export type DataCode = {
    "id": number,
    "label": string,
    "code": "LUM" | "TEM"
}

export class SensorApi extends Api {

    private static _instance: SensorApi = new SensorApi(apiEndpoint);

    public static get instance() {
        return this._instance;
    }

    public async getSensors() {
        return (await this.get<Sensor[]>("/sensors")).data;
    }

    public async getData(sensor: Sensor): Promise<Data[]> {
        let {data: rawData} = await this.get<RawData[]>(`/data/${sensor.serial}`);
        return rawData.map(d => ({...d, date: new Date(d.date * 1000)}));
    }

    public async getDataCodes() {
        return (await this.get<DataCode[]>(`/data_codes`)).data;
    }

}
