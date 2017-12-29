package springscripts.springdataextinit.testmaterial.n1;

import org.springframework.stereotype.Service;
import springscripts.springdataextinit.testmaterial.Context3;
import springscripts.springdataextinit.testmaterial.Service3;

/**
 * @author fanshen
 * @since 2017/12/22
 */
@Service("service3Provider")
public class Service3Provider implements Service3 {
    @Override
    public Void apply(Context3 context3) {
        DataExt3_1 dataExt31 = context3.getDataExt();
        dataExt31.setStr(dataExt31.getStr() + "-1");
        return null;
    }
}
