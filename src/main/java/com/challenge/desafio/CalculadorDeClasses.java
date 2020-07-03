package com.challenge.desafio;

import com.challenge.annotation.Somar;
import com.challenge.annotation.Subtrair;
import com.challenge.interfaces.Calculavel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;

public class CalculadorDeClasses implements Calculavel {
cl
    public BigDecimal getValor(Object object, Field field) {
        try {
            if (field.getType() != BigDecimal.class)
                return BigDecimal.ZERO;
            else {
                field.setAccessible(true);
                return (BigDecimal) field.get(object);
            }
        } catch (IllegalAccessException e) {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal processarCalculoPorTipoAtributo(Object object, Class<? extends Annotation> tipoAtributo) {
        Class<?> classe = object.getClass();
        return Arrays.stream(classe.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(tipoAtributo))
                .map(field -> getValor(object, field))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal somar(Object object) {
        return processarCalculoPorTipoAtributo(object, Somar.class);
    }

    @Override
    public BigDecimal subtrair(Object object) {
        return processarCalculoPorTipoAtributo(object, Subtrair.class);
    }

    @Override
    public BigDecimal totalizar(Object object) {
        return somar(object).subtract(subtrair(object));
    }
}
