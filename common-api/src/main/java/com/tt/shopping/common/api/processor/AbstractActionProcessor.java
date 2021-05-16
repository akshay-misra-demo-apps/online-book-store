package com.tt.shopping.common.api.processor;

public abstract class AbstractActionProcessor<T> {

    public abstract T process(T entity);
}
