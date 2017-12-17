package com.github.winteryoung.yanwte2.core.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.github.winteryoung.yanwte2.core.spi.SurrogateCombinator;
import com.github.winteryoung.yanwte2.core.utils.Lazy;
import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author Winter Young
 * @since 2017/12/16
 */
public class CombinatorTreeCache {
    private CombinatorTreeCache() {}

    private static Cache<ServiceOrchestrator<?>, Lazy<Combinator>> cache =
            CacheBuilder.newBuilder().build();

    public static Lazy<Combinator> getLazyTree(ServiceOrchestrator<?> serviceOrchestrator) {
        try {
            return cache.get(serviceOrchestrator, () -> getLazyCombinatorTree(serviceOrchestrator));
        } catch (UncheckedExecutionException | ExecutionException e) {
            Throwables.throwIfUnchecked(e.getCause());
            throw new RuntimeException(e.getCause());
        }
    }

    private static Lazy<Combinator> getLazyCombinatorTree(
            ServiceOrchestrator<?> serviceOrchestrator) {
        return Lazy.of(serviceOrchestrator::tree);
    }
}
