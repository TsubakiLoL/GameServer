package org.tsubaki.Game.Object;

import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

/**
 * 游戏对象类型扫描注解
 * 用法：@GameObjectTypeScan("org.tsubaki")
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(GameObjectTypeScannerRegistrar.class)
public @interface GameObjectTypeScan {
    /**
     * 要扫描的基础包路径
     */
    String[] value() default {};

    /**
     * 要扫描的基础包路径（别名）
     */
    String[] basePackages() default {};
}