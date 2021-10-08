package com.winowsi.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/8 14:23
 */

public class ValueListConstraintValidator implements ConstraintValidator<ValueList,Integer>{

    Set<Integer> set=new HashSet<>(100);
    @Override
    public void initialize(ValueList constraintAnnotation){
        for (int val : constraintAnnotation.vals()) {
            set.add(val);
        }


    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return  set.contains(integer);
    }
}
