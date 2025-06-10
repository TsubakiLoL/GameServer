package org.tsubaki.Game.Object;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;


import java.util.Set;

public class GameObjectTypeScanner extends ClassPathBeanDefinitionScanner {

    public GameObjectTypeScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
        addIncludeFilter(new AnnotationTypeFilter(GameObjectType.class));
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> holders = super.doScan(basePackages);

        // 注册找到的所有 GameObject 类型
        for (BeanDefinitionHolder holder : holders) {
            BeanDefinition definition = holder.getBeanDefinition();
            try {
                Class<?> clazz = Class.forName(definition.getBeanClassName());
                if (GameObject.class.isAssignableFrom(clazz)) {
                    GameObject.registerGameObjectType(
                            (Class<? extends GameObject>) clazz
                    );
                    System.out.println("注册 GameObject 类型: " + clazz.getSimpleName());

                    // 修改bean定义，防止自动实例化
                    if (definition instanceof AbstractBeanDefinition) {
                        // 设置为原型模式，每次请求时才创建实例
                        ((AbstractBeanDefinition) definition).setScope(BeanDefinition.SCOPE_PROTOTYPE);
                        // 或者设置为延迟初始化
                        ((AbstractBeanDefinition) definition).setLazyInit(true);
                    }
                }
            } catch (ClassNotFoundException e) {
                logger.error("无法加载类: " + definition.getBeanClassName(), e);
            }
        }

        return holders;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        // 确保只处理顶级类（非内部类、接口等）
        return beanDefinition.getMetadata().isIndependent();
    }
}