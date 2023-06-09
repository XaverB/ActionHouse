﻿# ActionHouse

Small sample application to play around with JPA and Hibernate in Java.

## Quickstart

### Run the application

1. Run database with `mvn derby:run`
2. Run the actionhouse.app.ConsoleManagementTool with your IDEA.

I tried to run the application with a jar file packaged through maven.
The below steps do not work yet. The application can't find org/dbunit/IDatabaseTester.class. Even though I did not set the scope of the library to test.
1. Start derby db with `mvn derby:run`
2. Create jar with `mvn package -pl backend`
3. Run jar with `java -jar backend/target/backend-1.0-SNAPSHOT.jar`

### Run tests

Tests will run in memory with derby.
DBUnit is used to set up and validate the tests.

Run:
`mvn test -pl backend`

## Integration tests

Integration tests are executed against Derby with a defined dataset.
DBUnit is used to set up and validate the tests.
See: https://www.dbunit.org/howto.html

`actionhouse.backend.tools.DbUnitDataGenerator` is used to generate a XML dump from a derby db.

### Pitfalls DBUnit

- JUNIT5 and DBUnit do not work well together with their documentation default configuration. Use composition over inheritance.
- Default XML export from database does not work without modification. You have to take care of hibernate inheritance, enable nullable field flags and take care of the ordering.
- Default XNL export is in wrong order regarding foreign key constraints.
- Further investigating the documentations of DBUnit and StackOverflow deliver solutions. Anyway, if their recommend approach does not deal with foreign keys correctly, it is quite annoying.
  - https://stackoverflow.com/a/26118272
- I should have used a index parsing method for the XML data extraction instead of the row number. My approach is a bit confusing and high in maintenance.

Conclusion: I prefer dev containers for integration testing. Handling SQL files is easier for me than handling XML datasets.

## Side notes

- There is no dedicated PaymentRepository. Payments will be managed by cascading customer operations.

## UNIT tests

- Insight class is unit tested.
  - Only methods with logic are tested.
  - Most methods just call the repository.
  - The entity manager is mocked with mockito.
  - The repositories are stubbed.

## Known Issues

- insights.getTopArticles(count) throws a IndexOutOfBounds exception. The count value does not matter.
