# uroboroSQL Generator

<img src="https://future-architect.github.io/uroborosql-doc//images/logo.png" style="max-width: 600px;" alt="uroboroSQL" />

## Generate entity classes

### configuration
src/main/resources/entity-config.properties

```properties
# entity class's pacakge name
package.name=jp.co.future.uroborosql.model
# output directory
output.dir=./target/entities

include.table.pattern=
exclude.table.pattern=
include.column.pattern=
exclude.column.pattern=

# Entity class's Parent class name (optional)
base.model.name=jp.co.future.uroborosql.BaseModel
# Entity class's javadoc @author (optional)
author.name=uroborosql-generator
# Entity class's lock version column name (optional)
lock.version.name=lock_version
```

### execution

```
$ mvn -Pentity
```

## License

Released under the [MIT License](https://github.com/shout-star/uroborosql-generator/blob/master/LICENSE).

Copyright (c) 2017 KENICHI HOSHI.