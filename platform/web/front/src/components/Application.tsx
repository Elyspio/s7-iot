import * as React from 'react';
import {Paper} from "@material-ui/core";
import "./Application.scss"
import {connect, ConnectedProps} from "react-redux";
import {Dispatch} from "redux";
import {RootState} from "../store/reducer";
import {toggleTheme} from "../store/module/theme/action";
import Appbar from "./Appbar/Appbar";
import Brightness5Icon from '@material-ui/icons/Brightness5';
import Haproxy from "./test/Sensors";
import Drawer from "@bit/elyspio.test.drawer";

const mapStateToProps = (state: RootState) => ({theme: state.theme.current})

const mapDispatchToProps = (dispatch: Dispatch) => ({toggleTheme: () => dispatch(toggleTheme())})

const connector = connect(mapStateToProps, mapDispatchToProps);
type ReduxTypes = ConnectedProps<typeof connector>;

export interface Props extends ReduxTypes {
}

interface State {
}

class Application extends React.Component<Props, State> {

	render() {

		return (
			<Paper square={true} className={"Application"}>
				<Drawer position={"right"} actions={[{onClick: this.props.toggleTheme, text: "Switch lights", icon: <Brightness5Icon/>}]}>
					<div className="content">
						<Appbar appName={"Sensors manager"}/>
						<Haproxy/>
					</div>
				</Drawer>
			</Paper>
		);
	}
}

export default connector(Application)
