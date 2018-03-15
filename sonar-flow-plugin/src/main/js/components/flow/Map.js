import React from 'react';
import {getChildren} from './functions/flowUtils.js'

export default class Map extends React.PureComponent {
	//this.props.map
	//this.props.issueLine
	constructor(props) {
		super(props);
	}
	
	render(){
		let isSelected=this.props.issueLine==this.props.map.line;
		return (
			<li className={"map"+(isSelected?" selected":"")}>
				<span className="logo"></span>
				<span className="label">{this.props.map.label}</span>
				<span className="text">MAP</span>
				<span className="comment">{this.props.map.comment!=null && (this.props.map.comment)}</span>
				<span className="lineNr">{this.props.map.line}</span>
			</li>
		);
	}
}