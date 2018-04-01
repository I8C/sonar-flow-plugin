import React from 'react';
import {getChildren} from './functions/flowUtils.js'

export default class Loop extends React.PureComponent {
	//this.props.loop
	//this.props.issueLine
	constructor(props) {
		super(props);
	}
	
	render(){
		const children = getChildren(this.props.loop,this.props.issueLine);
		let isSelected=this.props.issueLine==this.props.loop.line;
		return (
			<li className={"loop"+(isSelected?" selected":"")}>
				<span className="logo"></span>
				<span className="label">{this.props.loop.label}</span>
				<span className="text">LOOP</span>
				<span className="comment">{this.props.loop.comment!="" && ("("+this.props.loop.comment+")")}</span>
				<span className="lineNr">{this.props.loop.line}</span>
				<ul>
					{children}
				</ul>
			</li>
		);
	}
}