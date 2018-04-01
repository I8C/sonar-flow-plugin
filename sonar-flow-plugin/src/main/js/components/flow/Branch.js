import React from 'react';
import {getChildren} from './functions/flowUtils.js'

export default class Branch extends React.PureComponent {
	//this.props.branch
	//this.props.issueLine
	constructor(props) {
		super(props);
	}
	
	render(){
		const children = getChildren(this.props.branch,this.props.issueLine);
		let isSelected=this.props.issueLine==this.props.branch.line;
		return (
			<li className={"branch"+(isSelected?" selected":"")}>
				<span className="logo"></span>
				<span className="label">{this.props.branch.label}</span>
				<span className="text">BRANCH</span>
				<span className="comment">{this.props.branch.comment!="" && ("("+this.props.branch.comment+")")}</span>
				<span className="lineNr">{this.props.branch.line}</span>
				<ul>
					{children}
				</ul>
			</li>
		);
	}
}