import org.jazzteam.Main;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testMain() {
        Main.main(new String[]{});
        assertTrue(true);
    }
}

