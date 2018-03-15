import {getJSON} from 'sonar-request'; 

export function findIssues(projectKey) {
  return getJSON('/api/issues/search', {
		resolved : false,
		componentKeys : projectKey
	}).then(function (response) {
     return response;
  });
}

export function getFlow(projectKey) {
	  return getJSON('/api/flowsources/flow', {
		  	key : projectKey,
		  	type: 'json'
		}).then(function (response) {
	     return response;
	  });
	}