import React from 'react';
import {render} from 'react-dom';

import injectTapEventPlugin from 'react-tap-event-plugin';

import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import AppBar from 'material-ui/AppBar';

import Request from './components/Request';
import DocumentationProcessor from './components/DocumentationProcessor';

class App extends React.Component {
	constructor(props) {
		super(props); 
		
		this.state = {
			request: ""
		}
	}

	createRequest = (url) => {
		var payload = {
			url: url,
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

	render () {
		return (
			<MuiThemeProvider>
				<div>
					<AppBar
						title="Code Insights" />
					<Request 
						callback={this.createRequest}/>
					<DocumentationProcessor 
						request={this.state.request}/>
				</div>
			</MuiThemeProvider>
		);
	}
}

injectTapEventPlugin();
render(<App/>, document.getElementById('app'));
