import React from 'react';
import FlowIssuesList from './FlowIssuesList.js';
import FlowIssuesViewer from './FlowIssuesViewer.js';

export default class FlowIssuesApp extends React.PureComponent {
	constructor(props) {
		super(props);
		// this.props.component
		this.state = {
			selectedIssue: {key:"n/a"}
		};
		this.handleSelection = this.handleSelection.bind(this);
	}
  
	handleSelection(issue) {
		this.setState({selectedIssue: issue});
	}
	
	render() {
	  	const projectKey = this.props.component.key;
	    return (
	      <div className="layout-page issues" id="issues-page">

	      <div>
          	<div className="layout-page-side-inner">
          		<FlowIssuesList projectKey={projectKey} onSelection={this.handleSelection} selectedIssue={this.state.selectedIssue}/>
          	</div>
          </div>

	        <div className="layout-page-main">
	          <div className="layout-page-header-panel layout-page-main-header issues-main-header">
	            <div className="layout-page-header-panel-inner layout-page-main-header-inner">
	              <div className="layout-page-main-inner">
	                  <div className="pull-left width-60">
	                  {this.state.selectedIssue.message!=null?this.state.selectedIssue.message:"Nothing selected"}
	                  </div>
	              </div>
	            </div>
	          </div>

	          <div className="layout-page-main-inner">
	            <div>
	              {this.state.selectedIssue ? (
	                <FlowIssuesViewer issue={this.state.selectedIssue} />
	              	) : (
	                <div/>
	              )}
	            </div>
	          </div>
	        </div>
	      </div>
	    );
  }
}