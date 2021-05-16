package com.tt.shopping.common.api.validator.factory;

import com.tt.shopping.common.api.validator.Validator;

import java.util.List;

public interface ValidatorFactory {

    List<Validator> getPreValidators(String action);

    List<Validator> getPostValidators(String action);
}
