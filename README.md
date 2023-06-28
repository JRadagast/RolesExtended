# RolesExtended
Project to create a Role Extension to a User and Team API

As it was described in the Challenge document, the objective was creating an extension to the User and Team services by adding a new Role service.

First of all, it was created the base projects and the database.

# Installing

For the database created, see file in the project root: database.sql

This application was build using Spring Boot version 2.7.13 and maven.
Also it requires java version 11.

To run the application, it is necessary to configure your database information.
Check both application.properties and SpringJdbcConfig.

Using Netbeans, all it is needed is to run the project. Cleaning and Building recommended.
If it does not run, go to > Project Properties > Run and check if MainClass is set.

for IntelliJ or Command Line run, you will need to set the parameter execMainClass.

# Description

To solve the challenge presented, it was created the methods to request data from the urls given in the document to populate the database. 
It was created the endpoint: "/rolesextension/retrievedata" to get all this information, and it is in this same methods that the initial roles, and the default role, are created.

For this new role service, it was created a table for the roles containing an ID, its Name and if it is the default role. 
And it was added the IDROLE into the user table.
It was also defined the following endpoints:

POST: create new role: "/rolesextension/role/saverole"
It requires a json passed through body containing the following information.

```
{
	"name": "NEW_ROLE_NAME",
	"isdefault": 0
}
```

This url will return the added role.

Field Name is the role's description.
Field isDefault defines if a role will be considered the default role in the system.
Whenever a new user is added, if it does not possess a role yet, the role with isdefault as true (1) will be chosen.

If a new role is added with isdefault as true (1), the role service will change every other role that had isdefault set as true (1) to false (0).

To update a user role:
POST: "/rolesextension/user/updaterole"
Body: 

```
{
	"id": "USER_ID",
	"idrole": ID_ROLE
}
```

This url will return the updated User.

To look up a role for a Membership:
GET: "/rolesextension/role/getrolebymembership?iduser={IDUSER}&idteam={IDTEAM}"

and finally, to look up all memberships with a specific role:
GET: "/rolesextension/membership/getmembershipsbyrole?idrole={IDROLE}"

## Sugestions 

For both the user and team services, it may be a good idea to create a new, numeric value, as an ID for internal use only.
The reason for this is how resource intensive is comparing Strings in the database when compared to comparing two numbers.

For the Team service, it would be a good idea to add the teamLeadId in the get Teams url, as that information is very important to the team data.

For the User service, now that the user role was implemented, it may be necessary to add the roleId to both get User and get Users urls.

