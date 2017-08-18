import React from 'react';

import {Card, CardActions, CardHeader, CardText} from 'material-ui/Card';

import LinearProgress from 'material-ui/LinearProgress';

export default class DocumentationProcessor extends React.Component {

	style = {
		marginTop: '20px',
	}

	render() {
		return (
			<Card
				style={this.style}>
				<CardHeader
					title="Documentation Processor"
					actAsExpander={true}
					showExpandableButton={true} />

				<CardActions>
					<LinearProgress mode="indeterminate" />
				</CardActions>

			</Card>
		);
	}
}
