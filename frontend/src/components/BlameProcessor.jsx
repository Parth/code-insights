import React from 'react';

import {Card, CardActions, CardHeader, CardText} from 'material-ui/Card';
import {Table,	TableBody, TableHeader, TableHeaderColumn, TableRowColumn, TableRow} from 'material-ui/Table';

import LinearProgress from 'material-ui/LinearProgress';

import Status from './Status';

export default class BlameProcessor extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			url: this.props.url,
			job: "",
			result: "",
			error: ""
		}
	}

	componentDidMount() {
		var payload = {
			url: this.state.url,
			processorType: "blameprocessor"
		};
		
		fetch("http://127.0.0.1:8000/create-job", 
		{
			method: "POST",
			body: JSON.stringify(payload)
		})
		.then(function(res) {return res.json(); })
		.then(this.handleNewJob);
	}

	handleNewJob = (data) => {
		if (data.error !== undefined) {
			this.setState({error: data});
		} else {
			this.setState({job : data});
		}
	}

	onComplete = (result) => {
		this.setState({result: result});
	}

	style = {
		marginTop: '20px',
	}

	render() {
		let table = null;
		//TODO key prop
		if (this.state.result !== "") {
			table = (
				<Table>
					<TableHeader>
						<TableRow>
							<TableHeaderColumn>Name</TableHeaderColumn>
							<TableHeaderColumn>Commits Contributed</TableHeaderColumn>
							<TableHeaderColumn>Lines Contributed</TableHeaderColumn>
							<TableHeaderColumn>Project Equity</TableHeaderColumn>
						</TableRow>
					</TableHeader>
					<TableBody>
						{this.state.result.map((datum) => (
							<TableRow>
								<TableRowColumn>{datum.name}</TableRowColumn>
								<TableRowColumn>{datum.commitsContributed}</TableRowColumn>
								<TableRowColumn>{datum.linesContributed}</TableRowColumn>
								<TableRowColumn>{datum.projectEquity}</TableRowColumn>
							</TableRow>
						))}
					</TableBody>
				</Table>
			);
		} else {
			table = (<div></div>);
		}

		let progress = null;
		
		if (this.state.error !== "") {
			progress = (<p>ERROR: {this.state.error}</p>)
		} else if (this.state.result === "") {
			progress = (<LinearProgress mode="indeterminate" />);
		} else {
			progress = (<div></div>);
		}

		let jobStatus = null; 
		if (this.state.job !== "") {
			jobStatus = (
				<Status 
					job={this.state.job}
					onComplete={this.onComplete}
					/>
				);
		} else {
			jobStatus = (<div></div>);
		}

		return (
			<Card
				expanded={true}
				style={this.style} >

				<CardHeader
					title="Blame Processor"
					subtitle="Show details"
					actAsExpander={true}
					showExpandableButton={true} />

				<CardActions>
					{progress}
				</CardActions>

				<CardText expandable={true}>
					{table}
					{jobStatus}
				</CardText>
			</Card>
		);
	}
}
