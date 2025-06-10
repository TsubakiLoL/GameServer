package org.tsubaki.Game.Object;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.*;

public class GameObjectTypeScannerRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(
            AnnotationMetadata importingClassMetadata,
            BeanDefinitionRegistry registry) {

        // 获取 @GameObjectTypeScan 注解的属性
        Set<String> basePackages = getBasePackages(importingClassMetadata);

        // 创建并配置扫描器
        GameObjectTypeScanner scanner = new GameObjectTypeScanner(registry);
        scanner.doScan(StringUtils.toStringArray(basePackages));
    }

    private Set<String> getBasePackages(AnnotationMetadata metadata) {
        // 获取注解属性
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                metadata.getAnnotationAttributes(GameObjectTypeScan.class.getName())
        );
        // 获取 value 和 basePackages 属性
        String[] value = attributes.getStringArray("value");
        String[] basePackages = attributes.getStringArray("basePackages");
        // 合并所有包路径
        List<String> packages = new ArrayList<>();
        packages.addAll(Arrays.asList(value));
        packages.addAll(Arrays.asList(basePackages));
        // 如果没有指定包，则使用启动类所在包
        if (packages.isEmpty()) {
            String defaultPackage = ClassUtils.getPackageName(metadata.getClassName());
            packages.add(defaultPackage);
        }

        return new LinkedHashSet<>(packages);
    }
}