## DEVELOPMENT SETUP

### Get the source code
Clone this repository to your local machine.
```sh
git clone https://github.com/I8C/sonar-flow-plugin
```
Open eclipse and import the sonar-flows-plugin and sonar-flow-plugin-sslr maven projects.

### Develop your flow code checks
Now you are ready to develop your own checks. Use the following flow code checks in [this folder](sonar-flow-plugin/src/main/java/be/i8c/codequality/sonar/plugins/sag/webmethods/flow/check) as example.

### Build the plugin jar
Build the webMethods flow-code plugin jar which we will add as a plugin to the sonarqube server. The generated jar is located under the target directory.
![eclise build](assets/development_tutorial/devtut_4.png)
