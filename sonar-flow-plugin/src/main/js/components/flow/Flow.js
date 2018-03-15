import React from 'react';
import {getChildren} from './functions/flowUtils.js'

export default class Flow extends React.PureComponent {
	//this.props.flow
	//this.props.issueLine
	constructor(props) {
		super(props);
	}
	
	isFirst(flow, issueLine){
		if(flow!=null && flow.children!=null && flow.children.length>0 && flow.children[0]!=null){
			let firstLine = flow.children[0][Object.keys(flow.children[0])[0]].line;
			if(issueLine<firstLine)
				return true;
		}
		return false;
	}
	
	isLast(flow, issueLine){
		if(flow!=null && flow.children!=null && flow.children.length>0 && flow.children[flow.children.length-1]!=null){
			let lastChild = flow.children[flow.children.length-1][Object.keys(flow.children[flow.children.length-1])[0]];
			if(lastChild.children!=null && lastChild.children.length>0){
				return this.isLast(lastChild, issueLine);
			}else{
				let lastLine = lastChild.line;
				if(issueLine>lastLine)
					return true;
			}
		}
		return false;
	}
	
	render(){
		const children = getChildren(this.props.flow,this.props.issueLine);
		const isFirst = this.isFirst(this.props.flow,this.props.issueLine);
		const isLast = this.isLast(this.props.flow,this.props.issueLine);
		return (
			<ul className="flow">
				<li className={"pre" + (isFirst ? " selected":"")} />
				{children}
				<li className={"post" + (isLast ? " selected":"")} />
			</ul>
		);
	}
}