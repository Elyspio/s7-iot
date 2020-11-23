import React from 'react';
import "./Sensors.scss"
import {Data, Sensor} from "../../api/sensor";
import dayjs from "dayjs";
import {Box} from "@material-ui/core";
import {Line} from '@reactchartjs/react-chart.js'
import {ChartOptions} from "chart.js"

export type GraphProps = {
    data: ({
        [key in Sensor["serial"]]: Data["value"];
    } & {
        date: number
    })[],
    unit?: string,
    title: string,
    colors: [number, number, number][]
};


/**
 * Display all values from Sensor for a data type (temperature, luminosity)
 * @constructor
 */
export const Graph = ({data, colors, title}: GraphProps) => {

    const show = React.useMemo(() => {
        let fields = new Set<string>();
        data.forEach(d => Object.keys(d).filter(f => f !== "date").forEach(f => fields.add(f)))
        console.log("Field", fields);
        return ({

            labels: data.map(d => dayjs(d.date).format("HH:mm:ss")),
            datasets: [...fields].map((f, i) => {

                return ({
                    label: f,
                    data: data.map(d => Number.parseFloat(d[f])),
                    fill: false,
                    backgroundColor: `rgb(${[i][0]}, ${colors[i][1]}, ${colors[i][2]})`,
                    borderColor: `rgb(${[i][0]}, ${colors[i][1]}, ${colors[i][2]})` ?? `rgba(${colors[i][0]}, ${colors[i][1]}, ${colors[i][2]}, 0.7)`,
                    yAxisID: 'y-axis-' + title,
                });
            })
        });
    }, [data, title])

    console.log("show", show)

    const options: ChartOptions = {
        scales: {
            yAxes: [
                {
                    type: 'linear',
                    display: true,
                    position: 'left',
                    id: 'y-axis-' + title,
                    ticks: {
                        beginAtZero: true,
                    }
                },
            ],
            xAxes: [{
                ticks: {
                    autoSkip: true,
                    maxTicksLimit: 30
                }
            }]
        },
    }

    return <Box className="Graph">
        <Line data={show} options={options} type={"line"}/>
    </Box>
}




