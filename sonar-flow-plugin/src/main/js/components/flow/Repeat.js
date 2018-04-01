import React from 'react';
import {getChildren} from './functions/flowUtils.js'

export default class Repeat extends React.PureComponent {
	//this.props.repeat
	//this.props.issueLine
	constructor(props) {
		super(props);
	}
	
	render(){
		const children = getChildren(this.props.repeat,this.props.issueLine);
		let isSelected=this.props.issueLine==this.props.repeat.line;
		return (
			<li className={"repeat"+(isSelected?" selected":"")}>
				<span className="logo"></span>
				<span className="label">{this.props.repeat.label}</span>
				<span className="text">REPEAT</span>
				<span className="comment">{this.props.repeat.comment!="" && ("("+this.props.repeat.comment+")")}</span>
				<span className="lineNr">{this.props.repeat.line}</span>
				<ul>
					{children}
				</ul>
			</li>
		);
	}
}