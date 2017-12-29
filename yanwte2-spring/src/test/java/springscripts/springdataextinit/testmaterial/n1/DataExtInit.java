package springscripts.springdataextinit.testmaterial.n1;

import com.github.winteryoung.yanwte2.core.DataExtensionInitializer;
import org.springframework.stereotype.Service;
import springscripts.springdataextinit.testmaterial.Context3;

/**
 * @author fanshen
 * @since 2017/12/22
 */
@Service
public class DataExtInit implements DataExtensionInitializer<Context3, DataExt3_1> {
    @Override
    public DataExt3_1 createDataExtension(Context3 extensibleData) {
        DataExt3_1 dataExt3_1 = new DataExt3_1();
        dataExt3_1.setStr("test");
        return dataExt3_1;
    }
}
