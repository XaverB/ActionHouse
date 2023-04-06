# ActionHouse

Small sample application to play around with JPA and Hibernate in Java.

## Integration tests

Integration tests are executed against Derby with a defined dataset.
DBUnit is used to set up and validate the tests.
See: https://www.dbunit.org/howto.html

`actionhouse.backend.tools.DbUnitDataGenerator` is used to generate a XML dump from a derby db.

### Pitfalls DBUnit

- JUNIT5 and DBUnit do not work well together with their documentation default configuration. Use composition over inheritance.
- If you do not want to do full table asserts, the parsing of expected rows is a lot of boiler plate code.
- Default XML export from database does not work without modification. You have to take care of hibernate inheritance, enable nullable field flags and take care of the ordering.
- Default XNL export is in wrong order regarding foreign key constraints.

Conclusion: Stay with dev containers for integration testing.