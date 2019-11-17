package com.yuyuko.mall.test.autoconfigure.dubbo;

import com.yuyuko.mall.test.autoconfigure.elasticsearch.ElasticsearchTest;
import org.springframework.boot.test.autoconfigure.filter.AnnotationCustomizableTypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.util.Collections;
import java.util.Set;

public class DubboTypeExcludeFilter extends AnnotationCustomizableTypeExcludeFilter {

    private final DubboTest annotation;

    DubboTypeExcludeFilter(Class<?> testClass) {
        this.annotation = AnnotatedElementUtils.getMergedAnnotation(testClass, DubboTest.class);
    }

    @Override
    protected boolean hasAnnotation() {
        return this.annotation != null;
    }

    @Override
    protected ComponentScan.Filter[] getFilters(FilterType type) {
        switch (type) {
            case INCLUDE:
                return this.annotation.includeFilters();
            case EXCLUDE:
                return this.annotation.excludeFilters();
            default:
                throw new IllegalStateException("Unsupported type " + type);
        }
    }

    @Override
    protected boolean isUseDefaultFilters() {
        return this.annotation.useDefaultFilters();
    }

    @Override
    protected Set<Class<?>> getDefaultIncludes() {
        return Collections.emptySet();
    }

    @Override
    protected Set<Class<?>> getComponentIncludes() {
        return Collections.emptySet();
    }

}
