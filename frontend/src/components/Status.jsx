import React from 'react';

import {Card, CardActions, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';

export default class Status extends React.Component {

	constructor(props) {
		super(props);
		this.state = { 
			job: this.props.job,
			jobStatus: "",
			error: ""
		}
	}

	componentDidMount() {
		checkOnJob();
	}

	checkOnJob = () => {
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

	processStatus = (data) => {
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

	getResult = () => {
		fetch("http://127.0.0.1:8000/job-result", 
		{
			method: "POST",
			body: JSON.stringify(this.state.job)
		})
		.then(function(res) {return res.json(); })
		.then(this.props.onFinish);
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
					title="Job Status Log"/>
				
				<CardText>
					{this.state.jobStatus}
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
