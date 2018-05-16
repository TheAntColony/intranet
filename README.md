Basic MVC aplication with React on front-end that uses Play framework with Scala language, Slick for database connection as FRM.

### `sbt run`

Application is bootstraped and built with sbt run command, it will start dependencies download and project build first back-end and then react front-end. If there is problem with sbt run command first install `npm i yarn` then `yarn install` this will install frontend dependencies, after that return to root folder and trigger `sbt run`, that is what I had to do on Windows PC.

## Application dependencies

For application to run you need Java SDK 1.8 and sbt build tool.
https://www.playframework.com/documentation/2.6.x/Installing

Intellij Community Edition has solid support for Scala with plugins
https://www.jetbrains.com/idea/download/#section=windows

## Backend

Backend is built as N-tier structure with 3 tiers DAL, BLL and WebApi.
We are using `[Slick](http://slick.lightbend.com/doc/3.2.3/) FRM` with `[Postgres](https://www.postgresql.org/docs/10/static/index.html)` database driver to connect to database and make queries on data. 

`DAL` layer has table mapping that maps postgreSQL that enables TableQuery with `Slick`, it has Traits for easier mocking and Repository that implements that Traits with DAL Models that represent database sql tables because FRM doesn't work as ORM and you need to connect everything manually.

`BLL` has some basic mapping and some more complex logic that Awaits Future from database and is layer between database and WebApi, some method are just calling of repository methods without any logic and some have some logic and calling of multiple repository methods.It has it's own models because in most cases DAL and BLL models are not same and BLL has more complex objects that was not possible to make on DAL layer.

`WebApi` is mostly dumm calls to Service with basic validation of post data depending on Model 

SQL script for database creation, routes and config are in config folder.

## Frontend

Frontend is React code separated in containers, components, services, routes and utils folder structure

`Components` are views that don't hold state and get their props from parent component that handles state for them.
`Containers` are mostly parent containers with state management and API requested where needed.
`Routes` are set of mapping between container/componentsa and routes implemented with React V4 router.
`Services` are API calls with fetch library on backend API.
`Utils` are some helper objects like local storage wrapper, random generator etc.
