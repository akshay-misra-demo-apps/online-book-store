package com.tt.shopping.common.api.validator.factory.impl;

import com.tt.shopping.common.api.validator.PostValidator;
import com.tt.shopping.common.api.validator.PreValidator;
import com.tt.shopping.common.api.validator.Validator;
import com.tt.shopping.common.api.validator.factory.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ValidatorFactoryImpl implements ValidatorFactory {

    @Autowired
    ApplicationContext context;

    @Override
    public List<Validator> getPreValidators(final String action) {
        final Map<String, PreValidator> validators = this.context.getBeansOfType(PreValidator.class);
        return validators.entrySet().stream()
                .filter(entry -> entry.getValue().getActions().contains(action))
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparingInt(PreValidator::getOrder))
                .collect(Collectors.toList());
    }

    @Override
    public List<Validator> getPostValidators(final String action) {
        final Map<String, PostValidator> validators = this.context.getBeansOfType(PostValidator.class);
        return validators.entrySet().stream()
                .filter(entry -> entry.getValue().getActions().contains(action))
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparingInt(PostValidator::getOrder))
                .collect(Collectors.toList());
    }
}
