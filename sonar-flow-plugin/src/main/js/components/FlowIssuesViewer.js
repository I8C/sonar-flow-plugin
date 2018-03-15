import React from 'react';
import {getFlow} from '../functions/sonar_api.js'
import Flow from './flow/Flow.js';

export default class FlowIssuesViewer extends React.PureComponent {
	constructor(props) {
		super(props);
		//this.props.issue
		this.state = {
				flow:null,
				component:this.props.issue.component
		  };
	}
	
	componentDidMount() {
		this.checkChange();
	}
	
	checkChange(){
		if(this.state.component != this.props.issue.component){
			if(this.props.issue.component.endsWith("flow.xml")){
				getFlow(this.props.issue.component).then(
					(response) => {
						this.setState({
							flow: response.flow,
							component:this.props.issue.component
						});
					}
				);
			}
		}
	}
	
	render() {
		this.checkChange();
		return (
			<Flow flow={this.state.flow} issueLine={this.props.issue.line}/>
		);
	}
}