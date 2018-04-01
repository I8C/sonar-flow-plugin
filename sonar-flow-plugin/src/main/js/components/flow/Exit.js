import React from 'react';
import {getChildren} from './functions/flowUtils.js'

export default class Map extends React.PureComponent {
	//this.props.exit
	//this.props.issueLine
	constructor(props) {
		super(props);
	}
	
	render(){
		let isSelected=this.props.issueLine==this.props.exit.line;
		return (
			<li className={"exit"+(isSelected?" selected":"")}>
				<span className="logo"></span>
				<span className="label">{this.props.exit.label}</span>
				<span className="text">EXIT</span>
				<span className="comment">{this.props.exit.comment!="" && ("("+this.props.exit.comment+")")}</span>
				<span className="lineNr">{this.props.exit.line}</span>
			</li>
		);
	}
}