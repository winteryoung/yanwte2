package scripts.dataext.initdataext.testmaterial.simple.ns2;

import org.assertj.core.api.Assertions;
import scripts.dataext.initdataext.testmaterial.Context;
import scripts.dataext.initdataext.testmaterial.simple.Service2;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class Service2Provider1 implements Service2 {
    @Override
    public Integer apply(Context context) {
        DataExt dataExt = context.getDataExt();
        Assertions.assertThat(dataExt).isNull();
        return null;
    }
}
