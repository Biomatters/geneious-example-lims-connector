# Example LIMS Connector Plugin
An example plugin for connecting Geneious Prime to a LIMS system such that sequences can be submitted and retrieved. As
a demonstration, this just stores sequences in memory meaning the "LIMS" is wiped when Geneious is shutdown.

This is designed to be used as a template for building real LIMS integration plugins. To do so, start by doing all the
`//todo`'s in the code. The main part is to implement your own `LimsAdapter`.

## Installation
From the root folder run the following command:

    ./gradlew createPlugin

This will create the plugin under build/distributions
