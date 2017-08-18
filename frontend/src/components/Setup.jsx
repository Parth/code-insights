import React from 'react';

import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
import {Card, CardActions, CardHeader, CardText} from 'material-ui/Card';

import {
	Table,
	TableBody,
	TableHeader,
	TableHeaderColumn,
	TableRow,
	TableRowColumn,
} from 'material-ui/Table';

export default class DocumentationProcessor extends React.Component {
	
	constructor(props) {
		super(props);

		this.state = { 
			url: "",
			job: "",
			jobStatus: "",
			result: "",
			error: ""
		};

		this.checkOnJob = this.checkOnJob.bind(this);
		this.processStatus = this.processStatus.bind(this);
		this.sendJobToServer = this.sendJobToServer.bind(this);
		this.getResult = this.getResult.bind(this);
		this.processResult = this.processResult.bind(this);
	}

	checkOnJob = () => {
		console.log(this.state.job);
		
	}

	processJob = (data) => { 
		console.log(data);
		this.setState({ 
			job: data,
		});

		this.checkOnJob();
	}

	processResult = (data) => {
		this.setState({
			result: data
		});
	}

	
	render() {

		return (
			<div>
				<TextField 
					hintText="Repository URL" 
					onChange={(e) => this.setState({url: e.target.value})} /> 

				<RaisedButton 
					label="Submit" 
					primary={true} 
					onTouchTap={this.sendJobToServer} />
				<Card>
					<CardText>
						{this.state.jobStatus.statusLog}
					</CardText>
				</Card>
				{table}
			</div>
		);
	}
}
