package cases;

import org.junit.jupiter.api.Test;
import utils.BaseHooks;

public class PageObjectTest extends BaseHooks {

    @Test
    public void dummyOpenTest() {
        driver.get("https://events.epam.com/");
    }

}
