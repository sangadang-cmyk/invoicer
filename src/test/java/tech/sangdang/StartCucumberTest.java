package tech.sangdang;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber") // define this as a Cucumber test suite
@SelectClasspathResource("features") // locate the feature files
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "tech.sangdang.cucumber") // locate the step definitions
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-reports.html") // define the report format and location
//@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@integration")
public class StartCucumberTest {
}