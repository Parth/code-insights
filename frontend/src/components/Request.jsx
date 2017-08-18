import React from 'react';

import {Card, CardActions, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';

export default class Request extends React.Component {

	constructor(props) {
		super(props);
		this.state = { 
			url: ""
		}
	}

	style = {
		marginTop: '20px',
	}

	finalizeRequest = () => {
		this.props.callback(this.state.url);
	}

	render() {
		return (
			<Card
				style={this.style}>
				<CardHeader
					title="New Request"/>
				
				<CardText>
					<TextField
						hintText="Repository URL (https or ssh)."
						fullWidth={true}
						onChange={(e) => this.setState({url: e.target.value})}/>
				</CardText>

				<CardActions>
					<RaisedButton 
						primary={true} 
						label="Submit"
						onTouchTap={this.finalizeRequest}/>
				</CardActions>
			</Card>
		);
	}
}
