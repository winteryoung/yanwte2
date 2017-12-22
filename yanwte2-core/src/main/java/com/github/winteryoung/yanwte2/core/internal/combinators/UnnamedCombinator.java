package com.github.winteryoung.yanwte2.core.internal.combinators;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.internal.CombinatorTreeCache;
import com.github.winteryoung.yanwte2.core.internal.ServiceProviderLocators;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.github.winteryoung.yanwte2.core.internal.SurrogateCombinator;
import com.github.winteryoung.yanwte2.core.internal.utils.Lazy;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Winter Young
 * @since 2017/12/16
 */
public class UnnamedCombinator implements SurrogateCombinator {
    private ServiceOrchestrator<? extends Function<?, ?>> serviceOrchestrator;
    private Class<? extends Function<?, ?>> serviceType;

    public UnnamedCombinator(
            ServiceOrchestrator<? extends Function<?, ?>> serviceOrchestrator,
            Class<? extends Function<?, ?>> serviceType) {
        this.serviceOrchestrator = checkNotNull(serviceOrchestrator);
        this.serviceType = checkNotNull(serviceType);
    }

    @Override
    public List<Combinator> getSurrogateCombinators() {
        Lazy<Combinator> lazyTree = CombinatorTreeCache.getLazyTree(serviceOrchestrator);
        Combinator root = lazyTree.get();

        Map<String, Function<?, ?>> packageIndexedProviders =
                ServiceProviderLocators.locateAllProvidersIndexedByPackages(serviceType);

        Set<String> totalPackages =
                packageIndexedProviders
                        .values()
                        .stream()
                        .map(provider -> provider.getClass().getPackage().getName())
                        .collect(Collectors.toSet());

        Set<String> namedPackages = Sets.newHashSet();
        Set<Combinator> visitedNodes = Sets.newHashSet();
        depthFirstTraverse(root, visitedNodes, namedPackages);

        return Sets.difference(totalPackages, namedPackages)
                .stream()
                .map(
                        pkg -> {
                            Function<?, ?> provider = packageIndexedProviders.get(pkg);
                            return new ProviderCombinator(provider);
                        })
                .collect(Collectors.toList());
    }

    private void depthFirstTraverse(
            Combinator node, Set<Combinator> visitedNodes, Set<String> namedPackages) {
        if (visitedNodes.contains(node)) {
            return;
        }

        visitedNodes.add(node);

        if (node instanceof ProviderCombinator) {
            ProviderCombinator providerCombinator = (ProviderCombinator) node;
            namedPackages.add(providerCombinator.getProviderPackage());
        }

        List<Combinator> children = node.getChildren();
        if (children == null) {
            return;
        }

        for (Combinator child : children) {
            depthFirstTraverse(child, visitedNodes, namedPackages);
        }
    }
}
