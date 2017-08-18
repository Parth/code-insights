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

	sendJobToServer = () => {
		var payload = {
			url: this.state.url,
			processorType: "Documentation"
		};
		
		fetch("http://127.0.0.1:8000/create-job", 
		{
			method: "POST",
			body: JSON.stringify(payload)
		})
		.then(function(res) {return res.json(); })
		.then(this.processJob);
	}

	checkOnJob = () => {
		console.log(this.state.job);
		
		fetch("http://127.0.0.1:8000/check-job", 
		{
			method: "POST",
			body: JSON.stringify(this.state.job)
		})
		.then(function(res) {return res.json(); })
		.then(this.processStatus);
		if (this.state.jobStatus.statusCode !== 1) {
			setTimeout(this.checkOnJob, 500);
		}
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

	getResult = () => {
		console.log(this.state.job);
		
		fetch("http://127.0.0.1:8000/job-result", 
		{
			method: "POST",
			body: JSON.stringify(this.state.job)
		})
		.then(function(res) {return res.json(); })
		.then(this.processResult);
	}

	processStatus = (data) => {
		console.log(this.state);
		if (data.error !== undefined) {
			this.setState({
				error: data
			});
		} else {
			this.setState({
				jobStatus: data
			});

			if (data.statusCode === 1) {
				this.getResult();
			}
		}
	}
	
	render() {
		let table = null;
		if (this.state.result !== "") {
			table = (
			  <Table>
				<TableHeader>
				  <TableRow>
					<TableHeaderColumn>Name</TableHeaderColumn>
					<TableHeaderColumn>Email</TableHeaderColumn>
					<TableHeaderColumn>Methods Contributed</TableHeaderColumn>
					<TableHeaderColumn>Methods Documented</TableHeaderColumn>
					<TableHeaderColumn>Methods Not Documented</TableHeaderColumn>
				  </TableRow>
				</TableHeader>
				<TableBody>
					{this.state.result.map((datum) => (
						<TableRow>
							<TableRowColumn>{datum.name}</TableRowColumn>
							<TableRowColumn>{datum.email}</TableRowColumn>
							<TableRowColumn>{datum.methodsContributed}</TableRowColumn>
							<TableRowColumn>{datum.documentedMethods}</TableRowColumn>
							<TableRowColumn>{datum.undocumentedMethods}</TableRowColumn>
						</TableRow>
					))}
				</TableBody>
			  </Table>
			);
		} else {
			table = (<div></div>);
		}

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
