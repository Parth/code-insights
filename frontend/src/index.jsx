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

	render () {
		var processors = null;
		if (this.state.request === "") {
			processors = (<div></div>);
		} else {
			processors = (
				<DocumentationProcessor
					request={this.state.request} />
			);
		}
		return (
			<MuiThemeProvider>
				<div>
					<AppBar
						title="Code Insights" />
					<Request 
						callback={this.createRequest}/>
					
					{processors}
				</div>
			</MuiThemeProvider>
		);
	}
}

injectTapEventPlugin();
render(<App/>, document.getElementById('app'));
