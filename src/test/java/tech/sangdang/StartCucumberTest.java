package tech.sangdang;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber") // define this as a Cucumber test suite
@SelectClasspathResource("features") // locate the feature files
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "tech.sangdang.cucumber") // locate the step definitions
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-reports.html") // define the report format and location
public class StartCucumberTest {
}