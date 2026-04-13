# Run Configuration

This directory contains an optional IntelliJ run configurations for ease of use.

## Setup
1. Copy the example file:
```sh
Application.run.xml.example → Application.run.xml
```

2. Open `Application.run.xml`

3. Replace the placeholder values with your database credentials:
```xml
<option name="environmentProperties">
  <map>
    <entry key="SPRING_DATASOURCE_JDBCURL" value="your_jdbc_url" />
    <entry key="SPRING_DATASOURCE_USERNAME" value="your_username" />
    <entry key="SPRING_DATASOURCE_PASSWORD" value="your_password" />
  </map>
</option>
```

## Notes
* Do not commit `Application.run.xml`
* The `.example` file is the template
