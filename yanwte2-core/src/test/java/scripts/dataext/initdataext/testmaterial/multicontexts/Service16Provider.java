package scripts.dataext.initdataext.testmaterial.multicontexts;

/**
 * @author fanshen
 * @since 2017/12/19
 */
public class Service16Provider implements Service16 {
    @Override
    public String apply(Context16 context16) {
        context16.getDataExt();
        return context16.getI() + "";
    }
}
