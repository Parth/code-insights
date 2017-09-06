import React from 'react';

import {Card, CardActions, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';

export default class AvailableProcessors extends React.Component {

	constructor(props) {
		super(props);
		this.state = { 
			processors: ""
		}
	}

	componentDidMount() {
		fetch("http://127.0.0.1:8000/all-processors", 
		{
			method: "GET",
			body: JSON.stringify(this.state.job)
		})
		.then(function(res) {return res.json(); })
		.then(this.processData);
	}

	processData = (data) => {
		this.setState({
			processors: data.join(", ")
		});
	}

	style = {
		marginTop: '20px',
	}

	render() {
		return (
			<Card
				style={this.style}>
				<CardHeader
					title="Available Processors"/>
				
				<CardText>
					{this.state.processors}
				</CardText>
			</Card>
		);
	}
}
