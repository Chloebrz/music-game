UV5 Music Game
==============

##Description

This is a music game developed by Telecom Bretagne students in F2B UV506. The goal is to guess the title and the artist of a music before the other players.

This project uses a Node.js web application using Socket.io, a Java control point (for the connected objects) using the UPnP protocol and a Fuseki server.


##Run the game

###Web application

To run the web application, you need to install Node.js. Then, in the `WebApp` folder, run :

    npm install

This will install the npm dependencies.

To run the server of the web application, simply run :

    node server.js

###Control point

For the control point, create a new Eclipse project and add the source files from the `ControlPoint` folder. Add the external libraries found in this archive of the `ControlPoint/lib` folder to your project. And simply run `MyControlPoint.java`.

###Fuseki server

Download an Apache Jena Fuseki server from this url : http://jena.apache.org/download/#apache-jena-fuseki. Extract the files and use the following command line to run the server :

    ./fuseki-server --update --mem /ds

Once your server is running, go to http://localhost:3030/manage.html. Select "Add new dataset" and create a new dataset with the name "blindtest". Then click on "Upload data", add the `data.rdf` file and "Upload all".

You're ready to play!
