package com.github.winteryoung.yanwte2.core.internal;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author fanshen
 * @since 2017/12/28
 */
public class ServiceOrchestrators {
    private static class NodeImpl implements ServiceOrchestrator.Node {
        private String name;
        private List<ServiceOrchestrator.Node> children;

        @Override
        public String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        @Override
        public List<ServiceOrchestrator.Node> getChildren() {
            return children;
        }

        void setChildren(List<ServiceOrchestrator.Node> children) {
            this.children = ImmutableList.copyOf(children);
        }

        @Override
        public String toString() {
            return "NodeImpl{" +
                    "name='" + name + '\'' +
                    ", children=" + children +
                    '}';
        }
    }

    public static ServiceOrchestrator.Node getExpandedTree(
            ServiceOrchestrator<?> serviceOrchestrator) {
        Combinator combinator = serviceOrchestrator.tree();
        if (combinator == null) {
            return null;
        }

        return toNodes(combinator).collect(Collectors.toList()).get(0);
    }

    private static Stream<ServiceOrchestrator.Node> toNodes(Combinator combinator) {
        if (combinator instanceof SurrogateCombinator) {
            SurrogateCombinator surrogateCombinator = (SurrogateCombinator) combinator;
            List<Combinator> surrogates = surrogateCombinator.getSurrogateCombinators();
            return surrogates.stream().flatMap(ServiceOrchestrators::toNodes);
        } else {
            NodeImpl node = new NodeImpl();
            node.setName(combinator.getName());

            node.setChildren(
                    combinator
                            .getChildren()
                            .stream()
                            .flatMap(ServiceOrchestrators::toNodes)
                            .collect(Collectors.toList()));

            return ImmutableList.of((ServiceOrchestrator.Node) node).stream();
        }
    }
}
