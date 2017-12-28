package scripts.expandedtree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import scripts.expandedtree.testmaterial.Service17;

/**
 * @author fanshen
 * @since 2017/12/28
 */
public class ExpandedTreeTests {
    @Test
    public void test() throws JsonProcessingException {
        Service17 service17 = ServiceOrchestrator.getOrchestrator(Service17.class);
        ServiceOrchestrator.Node node = service17.getExpandedTree();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(node);
        Assertions.assertThat(json)
                .isEqualTo(
                        "{\"name\":\"mapReduce\",\"children\":[{\"name\":\"chain\",\"children\":[{\"name\":\"scripts.expandedtree.testmaterial.Service17Provider1\",\"children\":[]},{\"name\":\"scripts.expandedtree.testmaterial.Service17Provider2\",\"children\":[]},{\"name\":\"scripts.expandedtree.testmaterial.Service17Provider3\",\"children\":[]}]},{\"name\":\"scripts.expandedtree.testmaterial.Service17Provider3\",\"children\":[]},{\"name\":\"scripts.expandedtree.testmaterial.n4.Service17Provider4\",\"children\":[]}]}");
    }
}
