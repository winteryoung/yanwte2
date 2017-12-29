package base;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import springscripts.TestApp;

/**
 * @author fanshen
 * @since 2017/12/22
 */
@SpringBootTest(classes = TestApp.class)
public abstract class TestBase extends AbstractTestNGSpringContextTests {}
