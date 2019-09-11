import com.accenture.huaweigroup.Application;
import com.accenture.huaweigroup.business.ChessManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ChessManagerTest {

    @Autowired
    private ChessManager chessManager;

    @Test
    public void randomChessTest() throws Exception {
        System.out.println(chessManager.getRandomChess());
    }
}
