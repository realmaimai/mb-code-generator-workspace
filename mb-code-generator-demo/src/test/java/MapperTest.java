import com.maimai.RunApplication;
import com.maimai.entities.po.ProductInfo;
import com.maimai.entities.query.ProductInfoQuery;
import com.maimai.mappers.ProductInfoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest(classes = RunApplication.class)
@ExtendWith(SpringExtension.class)
public class MapperTest {
    @Resource
    private ProductInfoMapper<ProductInfo, ProductInfoQuery> productInfoMapper;

    @Test
    public void selectList() {
        ProductInfoQuery selectQuery = new ProductInfoQuery();
        selectQuery.setCodeFuzzy("4");
        // select list
        List<ProductInfo> productInfoList = productInfoMapper.selectList(selectQuery);
        productInfoList.forEach(System.out::println);
        // select count
        Long count = productInfoMapper.selectCount(new ProductInfoQuery());
        System.out.println(count);
    }

}
