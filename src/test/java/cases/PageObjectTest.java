package cases;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.BaseHooks;

@Slf4j
public class PageObjectTest extends BaseHooks {

    private static final Logger logger = LoggerFactory.getLogger(PageObjectTest.class);

    @Test
    public void dummyOpenTest() {
        driver.get("https://events.epam.com/");
        logger.info("Открыли сайт");
    }

}
