## Synopsis

This project provides sonarQube plugin for the webMethods flow language.

The plugin comes with following predefined rules:

* SavePipelineCheck
* DisabledCheck
* TryCatchCheck


## Installation

Build the project sonar-flow-plugin and place the generated jar in the plugins folder of your sonarQube installation. If your instance isn't configured for hot deployment, you need to restart your server.

Add the required rules to your quality profile.

## Usage

The plugin will check and scan flow.xml files. It will also scan the node.ndf files and add them to the corresponding flow.xml file as child.

## Development Documentation

You can find the documentation [here](DEVELOPMENT.md)
