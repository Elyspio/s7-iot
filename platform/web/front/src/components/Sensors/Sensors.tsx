import React from 'react';
import "./Sensors.scss"
import {Data, DataCode, Sensor, SensorApi} from "../../api/sensor";
import {Graph, GraphProps} from "./Graph";
import Accordion from '@material-ui/core/Accordion';
import AccordionSummary from '@material-ui/core/AccordionSummary';
import AccordionDetails from '@material-ui/core/AccordionDetails';
import Typography from '@material-ui/core/Typography';
import Container from "@material-ui/core/Container"
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';


const api = SensorApi.instance;

type Showed = { [key in DataCode["code"]]: GraphProps };

function getRandomColor(): [number, number, number] {
	const rng = () => ((Math.random() * 1000) % 256)
	return [rng(), rng(), rng()]
}

const Sensors = () => {

	const [data, setData] = React.useState<Data[]>([]);
	const [dataCode, setDataCode] = React.useState<DataCode[]>([]);
	const [sensors, setSensors] = React.useState<Sensor[]>([])
	React.useEffect(() => {
		api.getSensors().then(setSensors)
		api.getDataCodes().then(setDataCode)
	}, [])

	React.useEffect(() => {
		async function accessToApi() {
			const d = Array<Data>();
			for (let sensor of sensors) {
				d.push(...await api.getData(sensor))
			}
			setData(d)
		}

		accessToApi()
	}, [sensors])


	const showed: Showed = React.useMemo(() => {
		const codes = [...new Set(dataCode.map(x => x.code))];
		return codes.reduce((store, current) => {
			// @ts-ignore
			store[current] = data.filter(d => d.code.code === current).map(d => {
				return {
					date: d.date.getTime(),
					[d.sensor.label]: d.value
				}
			})
			return store
		}, {} as Showed);
	}, [data, dataCode])

	console.log("showed", {...showed})
	const colors = React.useMemo(() => sensors.map(() => getRandomColor()), [sensors])

	return (
		<Container className={"Sensors"}>

			{Object.keys(showed).map(s => {
				const dataCod = dataCode.find(dc => dc.code === s);
				const title = `Evolution of ${dataCod?.label}`

				const elem =
					<div className={"container"}>
						<Accordion className={"accordion"} key={s}>
							<AccordionSummary
								expandIcon={<ExpandMoreIcon/>}
								aria-controls="panel1a-content"
								id="panel1a-header"
							>
								<Typography variant={"h5"}>{title}</Typography>
							</AccordionSummary>
							<AccordionDetails>
								<Graph title={title} data={showed[s]} colors={colors}/>
							</AccordionDetails>
						</Accordion>
					</div>
				return showed[s].length && elem;
			})}
		</Container>
	);

}


export default Sensors
