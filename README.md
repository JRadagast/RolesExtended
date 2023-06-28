# RolesExtended
Project to create a Role Extension to a User and Team API

As it was described in the Challenge document, the objective was creating an extension to the User and Team services by adding a new Role service.

First of all, it was created the base projects and the database.

# Installing

For the database created, see file: database.sql

This application was build using Spring Boot version 2.7.13 and maven.
Also it requires java version 11.

To run the application, it is necessary to configure your database information.
Check both application.properties and SpringJdbcConfig.

Using Netbeans, all it is needed is to run the project. Cleaning and Building recommended.
If it does not run, go to > Project Properties > Run and check if MainClass is set.





After that, it was created the methods to request data from the urls given in the document to populate the database. 
It was created the endpoint: "/rolesextension/retrievedata" to get all this information, and it is in this same methods that the default roles are created.

For this new role service, it was added the IDROLE into the user table.
It was also defined the following endpoints:

POST: create new role: "/rolesextension/role/saverole"
