import React from 'react';
import { render, unmountComponentAtNode } from 'react-dom';
import FlowIssuesApp from './components/FlowIssuesApp';
import './styles/style.css';
import './styles/flow.css';

window.registerExtension('flow/flow_issues', options => {

  const { el } = options;

  render(
          <FlowIssuesApp
          component={options.component}
          />, el
  );

  return () => unmountComponentAtNode(el);
});