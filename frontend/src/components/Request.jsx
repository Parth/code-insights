import React from 'react';

import {Card, CardActions, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import FlatButton from 'material-ui/FlatButton';

export default class Request extends React.Component {

	style = {
		marginTop: '20px',
	}

	render() {
		return (
			<Card
				style={this.style}>
				<CardHeader
					title="Create a new Request"/>
				
				<TextField/>

				<CardActions>
					<FlatButton label="Submit"/>
				</CardActions>
			</Card>
		);
	}
}
