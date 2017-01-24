---
layout: page
title: Usage
---

## **Using Cyrface**

Cyrface tutorial - [pdf](/cyrface/public/Cyrface2.0Manual.pdf)

Cyrface Cytoscape App that provides a general interaction between Cytoscape and R. Cyrface offers a way to combine a friendly graphical interface within the Cytoscape environment with any R package. A GUI should benefit beginners and occasional users; as well as being useful for training and illustration purposes, it extends the accessibility of the tool to those not familiar with the R command line interface.

This way it’s possible to take advantage of the R environment (e.g. Bioconductor, CRAN, datasets...) all within Cytoscape.

Cyrface takes advantage of the available Cytoscape tools, such as the Command Line. The Command Line offers the users the ability to script basic commands in Cytoscape, such as import, display or modify networks through a simple command line. The great advantage of the Command Line is the ability of performing repetitive tasks automatically. By supporting this tool Cyrface extends the possibility of the users to integrate in their scripts methods developed in R together with common Cytoscape features. The Command Tool Dialog window can be used to dynamically execute the necessary R commands, which for instance can be of particular usefulness when debugging an script.

The following figure shows an illustrative example using the Command Line Dialog tool to plot some features of an existing and publicly available data-set termed, iris, using the widely known plotting library ggplot.

<img src="/cyrface/public/CyrfacePlotCommandLine.png" alt="Cyrface example" style="width:500px;height:500px;">

To check which commands are made available by Cyrface type:

> help cyrface

Then to get more detailed information about a specific command, for instance about the run command, type:

> help cyrface run


## **Implementation**

Rserve and RCaller libraries are supported in Cyrface by the usage of RserveHandler and RCallerHandler Java classes, respectively. Both classes extend the abstract class RHandler that contains the signature of all the necessary methods to establish and maintain a connection with R. Figure 1 depicts the hierarchical structure of the Java classes responsible for handling the connection between Java and R. Moreover, it depicts the connection points between these two different environments.

Developers interface with Cyrface through the RHandler abstract class. RHandler is extended by classes that integrate any java library to interact with R, e.g. RserveHandler class integrates Rserve library. Therefore, all future library implementations will have to extend RHandler (see figure below).

<img src="/cyrface/public/Diagram.png" alt="Cyrface example" style="width:600px;height:300px;">

In case the handlers supported by Cyrface are not adequate to the user needs, the users can implement their own handler by simply extending the RHandler class using any other library that fits best.

**Note:** It’s advisable to check first the libraries' webpages to understand which technology suits best the users needs (Rserve, RCaller).

Both RserveHandler or RCallerHandler have methods to run R commands. Therefore, the users should instantiate a handler and then run the desired command.
