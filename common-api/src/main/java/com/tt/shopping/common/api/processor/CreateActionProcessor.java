package com.tt.shopping.common.api.processor;

public abstract class CreateActionProcessor<T> extends AbstractActionProcessor<T> {

    @Override
    public T process(T entity) {
        this.preValidation(entity);
        T persisted = this.doAction(entity);
        this.postValidation(persisted);

        return persisted;
    }

    public abstract T doAction(T entity);

    public abstract void preValidation(T entity);

    public abstract void postValidation(T entity);
}
