import React from 'react';
import "./Sensors.scss"
import {Data, Sensor} from "../../api/sensor";
import dayjs from "dayjs";
import {Box} from "@material-ui/core";
import {Line} from '@reactchartjs/react-chart.js'


export type GraphProps = {
	data: ({
		[key in Sensor["serial"]]: Data["value"];
	} & {
		date: number
	})[],
	unit?: string,
	title: string
};

/**
 * Display all values from Sensor for a data type (temperature, luminosity)
 * @constructor
 */
export const Graph = ({data, unit, title}: GraphProps) => {

	const fields = Object.keys(data[0]).filter(f => f !== "date");


	const show = {
		labels: data.map(d => dayjs(d.date).format("HH:mm:ss")),
		datasets: fields.map(f => ({
			label: f,
			data: data.map(d => Number.parseFloat(d[f])),
			fill: false,
			backgroundColor: 'rgb(255, 99, 132)',
			borderColor: 'rgba(255, 99, 132, 0.2)',
			yAxisID: 'y-axis-' + title,
		}))
	}

	const options = {
		scales: {
			yAxes: [
				{
					type: 'linear',
					display: true,
					position: 'left',
					id: 'y-axis-' + title,
				},
			],
		},
	}

	return <Box className="Graph">
		<Line  data={show} options={options} type={"line"}/>
	</Box>
}




