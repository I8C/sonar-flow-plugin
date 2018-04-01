import React from 'react';
import {getChildren} from './functions/flowUtils.js'

export default class Invoke extends React.PureComponent {
	//this.props.invoke
	//this.props.issueLine
	constructor(props) {
		super(props);
	}
	
	render(){
		let isSelected=this.props.issueLine==this.props.invoke.line;
		return (
			<li className={"invoke"+(isSelected?" selected":"")}>
				<span className="logo"></span>
				<span className="label">{this.props.invoke.label}</span>
				<span className="text">INVOKE: {this.props.invoke.service}</span>
				<span className="comment">{this.props.invoke.comment!="" && ("("+this.props.invoke.comment+")")}</span>
				<span className="lineNr">{this.props.invoke.line}</span>
			</li>
		);
	}
}