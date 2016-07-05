# sonar-flow-plugin

This project provides sonarQube plugin for the webMethods flow language.

The plugin comes with following predefined rules:

* SavePipelineCheck
* DisabledCheck
* TryCatchCheck

## Installation

Take the plugin jar of this project and add it to your SonarQube server in the plugins folder (/opt/sonarqube/extensions/plugins/). If your instance isn't configured for hot deployment, you need to restart your server. Add the required rules to your quality profile.

## Development documentation
You can find the documentation to develop you own rules [here](sonar-flow-plugin-documentation/DEVELOPMENT.md).

## Development environment setup documentation
You can find documentation to setup the development environment [here](sonar-flow-plugin-documentation/DEVELOPMENT_SETUP.md).

## Usage
The plugin will check and scan flow.xml files. It will also scan the node.ndf files and add them to the corresponding flow.xml file as child.


