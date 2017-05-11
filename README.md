# The sonar-flow-plugin project

This project provides a SonarQube plugin for the webMethods flow language.

Currently the plugin comes with following predefined rules:

1. TryCatchCheck
   * A top-level service schould contain a try-catch. The top-level services should be able to catch exceptions and log, transform and obscure data from them.
2. SavePipelineCheck
   * Services should not contain any save- or restorePipeline services.
3. DisableCheck
   * Services should not contain any disabled elements.
4. InterfaceCommentsCheck
   * Interface elements should contain comments. This allows for correct creation of documentation.
5. QualityNameCheck
6. ExitCheck
   * Services with interface element 'EXIT' step need to be configured correctly. The "Exit from " property needs to be configured and the failure message needs to be added if the "Signal" property is set to "Failure".
7. EmptyMapCheck
   * Empty 'MAP' interface elements should be removed from the service.
8. EmptyFlowCheck
   * Empty services should be removed.
9. BranchPropertiesCheck
   * If the switch property of a BRANCH step is not defined, then the 'evaluate labels' property should be set to true. This indicates that the branching conditions are defined in the labels of the child step. If the switch is defined then the 'evaluate labels' property should be false or null.

## Quick setup guide
Here you'll find a [quick setup](sonar-flow-plugin-documentation/QUICK_SETUP.md).

## Develop your own flow code checks
You can find the documentation to develop you own rules [here](sonar-flow-plugin-documentation/DEVELOPMENT_SETUP.md).

## License
The sonar-flow-plugin is available under the [GNU Lesser General Public License v3.0](LICENSE.txt).
