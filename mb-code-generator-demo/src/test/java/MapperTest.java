import com.maimai.RunApplication;
import com.maimai.entities.po.ProductInfo;
import com.maimai.entities.query.ProductInfoQuery;
import com.maimai.mappers.ProductInfoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

    @Test
    public void insert() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode("80000");
        productInfo.setProductName("borger");
        productInfo.setPrice(BigDecimal.valueOf(123123));
        productInfo.setColorType(1);

        this.productInfoMapper.insert(productInfo);
    }

    @Test
    public void insertOrUpdate() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(8);
        productInfo.setProductName("test8");
        productInfo.setPrice(BigDecimal.valueOf(123123));
        productInfo.setColorType(1);

        productInfoMapper.insertOrUpdate(productInfo);
    }

    @Test
    public void batchInsert () {
        ArrayList<ProductInfo> productInfoList = new ArrayList<>();
        ProductInfo productInfo1 = new ProductInfo();
        productInfo1.setCreateDate(new Date());
        productInfo1.setCode("8888");
        productInfoList.add(productInfo1);
        ProductInfo productInfo2 = new ProductInfo();
        productInfo2.setCreateDate(new Date());
        productInfo2.setCode("7777");
        productInfoList.add(productInfo2);

        productInfoMapper.insertBatch(productInfoList);
    }

    @Test
    public void batchInsertOrUpdate () {
        ArrayList<ProductInfo> productInfoList = new ArrayList<>();
        ProductInfo productInfo1 = new ProductInfo();
        productInfo1.setCode("6666");
        productInfo1.setProductName("batch update test1");
        productInfoList.add(productInfo1);
        ProductInfo productInfo2 = new ProductInfo();
        productInfo2.setCode("8888");
        productInfo2.setProductName("batch update test2");
        productInfoList.add(productInfo2);

        productInfoMapper.insertOrUpdateBatch(productInfoList);
    }

    @Test
    public void selectByKey() {
        ProductInfo productInfo = productInfoMapper.selectById(6);
        System.out.println(productInfo.getProductName());
    }

    @Test
    public void updateByKey() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductName("update by 3");
        productInfoMapper.updateById(productInfo, 3);

        productInfo = new ProductInfo();
        productInfo.setProductName("update by 50000 code");
        productInfoMapper.updateByCode(productInfo, "50000");
    }

    @Test
    public void deleteByKey() {
        productInfoMapper.deleteById(4);
    }
}
