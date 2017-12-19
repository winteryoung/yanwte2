package scripts.dataext.initdataext.testmaterial.multicontexts;

/**
 * @author fanshen
 * @since 2017/12/19
 */
public class Service15Provider implements Service15 {
    @Override
    public String apply(Context15 context15) {
        context15.getDataExt();
        return context15.getI() + "";
    }
}
