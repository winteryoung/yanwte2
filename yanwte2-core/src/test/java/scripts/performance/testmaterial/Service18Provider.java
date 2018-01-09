package scripts.performance.testmaterial;

/**
 * @author fanshen
 * @since 2017/12/29
 */
public class Service18Provider implements Service18 {
    @Override
    public String apply(String s) {
        System.currentTimeMillis();
        return s;
    }
}
