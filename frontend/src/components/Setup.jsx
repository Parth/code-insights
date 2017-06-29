import React from 'react';

import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';

export default class Setup extends React.Component {
	
	constructor(props) {
		super(props);

		this.state = { 
			url: "",
			data: "",
			success: false
		};

		this.sendJobToServer = this.sendJobToServer.bind(this);
		this.updateState = this.updateState.bind(this);
	}

	updateState = (data) => { 
		this.setState({ 
			data: data.toString(),
			success: true
		});
	}


	sendJobToServer = () => { 
		var payload = {
			url: this.state.url
		};
		
		fetch("http://127.0.0.1:4567/repository-processor", 
		{
			method: "POST",
			body: this.state.url
		})
		.then(function(res) {return res.json(); })
		.then(this.updateState);
	};

	render () {
		let table = null;
		if (this.state.success) {
			table = 
				<Table>
				<Table>;
		} else {
			table = <div></div>;
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
				{table}
			</div>
		);
	}
}
