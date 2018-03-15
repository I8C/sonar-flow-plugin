import React from 'react';
import FlowIssueItem from './FlowIssueItem.js'
import {findIssues} from '../functions/sonar_api.js'

export default class FlowIssuesList extends React.PureComponent {

	constructor(props) {
		super(props);
		// this.props.projectKey
		// this.props.selectedIssue
		// this.props.onSelection
		this.state = {
			issues: [],
			components: []
		};
		this.handleSelection = this.handleSelection.bind(this);
	  }

  componentDidMount() {
	  findIssues(this.props.projectKey).then(
      (response) => {
        this.setState({
          issues: response.issues,
          components: response.components
        });
      }
    );
  }
  
  handleSelection(issue) {
	  this.props.onSelection(issue);
  }
  
  render() {
	  let issueItems = [];
	  let lastComponent=undefined;
	  this.state.issues.forEach(issue => {
		  issue.id=issue.key;
		  issueItems.push(
	  		<div>
	  		{issue.component!=lastComponent?<div className="concise-issue-component note text-ellipsis">{issue.component.replace(/.*:(.+?\/)(.+)(\/.+?\/.+)/,"$1...$3")}</div>:""}
			<FlowIssueItem {...issue} onSelection={this.handleSelection} isSelected={this.props.selectedIssue.id==issue.id}/>
			</div>
		  );
		  lastComponent=issue.component;
	  });
    return (
    		<div className="search-navigator-facets-list">
    			<div>{issueItems}</div>
    		</div>
    );
  }
}