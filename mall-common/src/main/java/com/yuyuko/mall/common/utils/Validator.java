package com.yuyuko.mall.common.utils;

import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;

public class Validator {
    public static final javax.validation.Validator validator =
            Validation.byProvider(HibernateValidator.class).configure()
                    .failFast(true).buildValidatorFactory().getValidator();

    public static <T> boolean validate(T obj) {
        if (obj == null)
            return false;
        Set<ConstraintViolation<T>> violationSet = validator.validate(obj);
        return !(violationSet != null && violationSet.size() > 0);
    }

}
