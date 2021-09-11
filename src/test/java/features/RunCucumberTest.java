package features;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber; //Junit test runner
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "html:target/cucumber-report.html"})
public class RunCucumberTest {

    // execute this to run the tests and
    // produce output in ./target directory

    // Handy for debugging and the Continuous Integration

}
