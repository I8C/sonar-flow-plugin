import React from 'react';
import {getChildren} from './functions/flowUtils.js'

export default class Sequence extends React.PureComponent {
	//this.props.sequence
	//this.props.issueLine
	constructor(props) {
		super(props);
	}
	
	render(){
		const children = getChildren(this.props.sequence,this.props.issueLine);
		let isSelected=this.props.issueLine==this.props.sequence.line;
		return (
			<li className={"sequence"+(isSelected?" selected":"")}>
				<span className="logo"></span>
				<span className="label">{this.props.sequence.label}</span>
				<span className="text">SEQUENCE</span>
				<span className="comment">{this.props.sequence.comment!=null && (this.props.sequence.comment)}</span>
				<span className="lineNr">{this.props.sequence.line}</span>
				<ul>
					{children}
				</ul>
			</li>
		);
	}
}