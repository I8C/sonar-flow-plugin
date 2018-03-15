import React from 'react';

export default class FlowIssueItem extends React.PureComponent {

	constructor(props) {
		super(props);
		// props.issue
		// props.onSelection
		// props.isSelected
		this.handleClick = this.handleClick.bind(this, this.props);
	  }
  
  handleClick(issue, e) {
	  this.props.onSelection(issue);
  }
  
  render() {
	const bug = <span className="little-spacer-right"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" width="16" height="16"><path d="M11 9h1.3l.5.8.8-.5-.8-1.3H11v-.3l2-2.3V3h-1v2l-1 1.2V5c-.1-.8-.7-1.5-1.4-1.9L11 1.8l-.7-.7-1.8 1.6-1.8-1.6-.7.7 1.5 1.3C6.7 3.5 6.1 4.2 6 5v1.1L5 5V3H4v2.3l2 2.3V8H4.2l-.7 1.2.8.5.4-.7H6v.3l-2 1.9V14h1v-2.4l1-1C6 12 7.1 13 8.4 13h.8c.7 0 1.4-.3 1.8-.9.3-.4.3-.9.2-1.4l.9.9V14h1v-2.8l-2-1.9V9zm-2 2H8V6h1v5z" style={{fill: 'currentcolor'}}></path></svg></span>;
	const codesmell = <span className="little-spacer-right"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" width="16" height="16"><path d="M8 2C4.7 2 2 4.7 2 8s2.7 6 6 6 6-2.7 6-6-2.7-6-6-6zm-.5 5.5h.9v.9h-.9v-.9zm-3.8.2c-.1 0-.2-.1-.2-.2 0-.4.1-1.2.6-2S5.3 4.2 5.6 4c.2 0 .3 0 .3.1l1.3 2.3c0 .1 0 .2-.1.2-.1.2-.2.3-.3.5-.1.2-.2.4-.2.5 0 .1-.1.2-.2.2l-2.7-.1zM9.9 12c-.3.2-1.1.5-2 .5-.9 0-1.7-.3-2-.5-.1 0-.1-.2-.1-.3l1.3-2.3c0-.1.1-.1.2-.1.2.1.3.1.5.1s.4 0 .5-.1c.1 0 .2 0 .2.1l1.3 2.3c.2.2.2.3.1.3zm2.5-4.1L9.7 8c-.1 0-.2-.1-.2-.2 0-.2-.1-.4-.2-.5 0-.1-.2-.3-.3-.4-.1 0-.1-.1-.1-.2l1.3-2.3c.1-.1.2-.1.3-.1.3.2 1 .7 1.5 1.5s.6 1.6.6 2c0 0-.1.1-.2.1z" style={{fill: 'currentcolor'}}></path></svg></span>;
	const vulnerability = <span className="little-spacer-right"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" width="16" height="16"><path d="M10.8 5H6V3.9a2.28 2.28 0 0 1 2-2.5 2.22 2.22 0 0 1 1.8 1.2.48.48 0 0 0 .7.2.48.48 0 0 0 .2-.7A3 3 0 0 0 8 .4a3.34 3.34 0 0 0-3 3.5v1.2a2.16 2.16 0 0 0-2 2.1v4.4a2.22 2.22 0 0 0 2.2 2.2h5.6a2.22 2.22 0 0 0 2.2-2.2V7.2A2.22 2.22 0 0 0 10.8 5zm-2.2 5.5v1.2H7.4v-1.2a1.66 1.66 0 0 1-1.1-1.6A1.75 1.75 0 0 1 8 7.2a1.71 1.71 0 0 1 .6 3.3z" style={{fill: 'currentcolor'}}></path></svg></span>;
    return (
    	<div className={"concise-issue-box clearfix" + (this.props.isSelected?" selected":"")} onClick={this.handleClick}>
			<div className="concise-issue-box-message">{this.props.message}</div>
			<div className="concise-issue-box-attributes"><span>{this.props.type.toLowerCase()=="bug" ? bug : (this.props.type.toLowerCase()=="codesmell" ? codesmell : (this.props.type.toLowerCase()=="vulnerability" ? vulnerability : ""))}{this.props.type}</span><span className="big-spacer-left"><i className={"icon-severity-" + this.props.severity.toLowerCase() + " little-spacer-right"}></i>{this.props.severity}</span></div>
		</div>
    );
  }
}