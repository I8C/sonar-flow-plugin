import React from 'react';
import Sequence from '../Sequence.js';
import Branch from '../Branch.js';
import Invoke from '../Invoke.js';
import Map from '../Map.js';
import Loop from '../Loop.js';
import Repeat from '../Repeat.js';
import Exit from '../Exit.js';

export function getChildren(element, issueLine){
	let output = [];
	if(element!=null && element.children!=null){
		element.children.forEach(child => {
			switch (Object.keys(child)[0]) {
				case 'sequence': output.push( <Sequence sequence={child.sequence} issueLine={issueLine} /> );break;
				case 'branch': output.push( <Branch branch={child.branch} issueLine={issueLine} /> );break;
				case 'invoke': output.push( <Invoke invoke={child.invoke} issueLine={issueLine} /> );break;
				case 'map': output.push( <Map map={child.map} issueLine={issueLine} /> );break;
				case 'loop': output.push( <Loop loop={child.loop} issueLine={issueLine} /> );break;
				case 'repeat': output.push( <Repeat repeat={child.repeat} issueLine={issueLine} /> );break;
				case 'exit': output.push( <Exit exit={child.exit} issueLine={issueLine} /> );break;
			}
		});
	}else
		output.push( <div className="noChildren"></div> );
	return output;
}