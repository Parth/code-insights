import React from 'react';

import {Card, CardActions, CardHeader, CardText} from 'material-ui/Card';

import LinearProgress from 'material-ui/LinearProgress';

export default class Request extends React.Component {

	render() {
		return (
			<Card>
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
