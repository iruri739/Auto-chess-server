import com.accenture.huaweigroup.Application;
import com.accenture.huaweigroup.module.entity.Chess;
import com.accenture.huaweigroup.module.mapper.ChessMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ChessMapperTest {

    @Autowired
    ChessMapper chessMapper;



    @Test
    public void insertTest() {
        Chess c = chessMapper.getAll().get(0);
        c.setName("test111");
        chessMapper.insert(c);
    }
}
