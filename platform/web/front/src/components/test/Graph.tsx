import React from 'react';
import "./Sensors.scss"
import {Data, Sensor} from "../../api/sensor";
import {ArgumentAxis, Chart, Legend, ScatterSeries, Title, ValueAxis} from "@devexpress/dx-react-chart-material-ui";
import {Box, createStyles, Paper, Theme, withStyles} from "@material-ui/core";
import dayjs from "dayjs";

export type GraphProps = {
	data: ({
		[key in Sensor["serial"]]: Data["value"];
	} & {
		date: Date
	})[],
	unit?: string,
	title: string
};

const format = () => tick => {
	const time = dayjs(tick).format("HH:mm:ss")
	console.log("tick", time);
	return time;
};


const legendStyles = (theme: Theme) => createStyles({
	root: {
		display: 'flex',
		margin: 'auto',
		flexDirection: 'row',
	},
})

const legendLabelStyles = (theme: Theme) => createStyles({
	label: {
		paddingTop: theme.spacing(1),
		whiteSpace: 'nowrap',
	},
})

const legendItemStyles = (theme: Theme) => createStyles({
	item: {
		flexDirection: 'column',
	},
})


const legendRootBase = ({classes, ...restProps}) => (
	<Legend.Root children={[]} {...restProps} className={classes.root}/>
);
const legendLabelBase = ({classes, ...restProps}) => (
	<Legend.Label text="" className={classes.label} {...restProps} />
);
const legendItemBase = ({classes, ...restProps}) => (
	<Legend.Item children={[]} className={classes.item} {...restProps} />
);
const Root = withStyles(legendStyles, {name: 'LegendRoot'})(legendRootBase);
const Label = withStyles(legendLabelStyles, {name: 'LegendLabel'})(legendLabelBase);
const Item = withStyles(legendItemStyles, {name: 'LegendItem'})(legendItemBase);

const ValueLabel = (props) => {
	console.log("PROPS", props);
	const {text} = props;
	return (
		<ValueAxis.Label
			{...props}
			text={`${text}%`}
		/>
	);
};

/**
 * Display all values from Sensor for a data type (temperature, luminosity)
 * @constructor
 */
export const Graph = ({data, unit, title}: GraphProps) => {

	const sData = [...data]
	const fields = Object.keys(sData[0]).filter(k => k !== "date");
	sData.sort((a, b) => {
		return a[fields[0]].localeCompare(b[fields[0]]);
	})

	return (
		<Box className="Graph">
			<Chart
				data={sData}>
				{fields.map(f => <ScatterSeries
					valueField={f}
					name={f[0].toUpperCase() + f.slice(1)}
					argumentField="date"
				/>)}
				{/*@ts-ignore*/}
				<Legend position="bottom" rootComponent={Root} labelComponent={Label}/>

				<ArgumentAxis tickFormat={format}/>
				<ValueAxis lineComponent={ValueLabel}/>
			</Chart>
		</Box>

	);

}
