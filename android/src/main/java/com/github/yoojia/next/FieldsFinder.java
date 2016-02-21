package com.github.yoojia.next;

import com.github.yoojia.events.supports.AnnotatedFinder;

import java.lang.reflect.Field;

/**
 * Field helper
 * @author 陈小锅 (chenyongjia@parkingwang.com)
 * @since 1.0
 */
public class FieldsFinder extends AnnotatedFinder<Field> {

    @Override
    protected Field[] resourcesFromType(Class<?> type) {
        return type.getDeclaredFields();
    }

}