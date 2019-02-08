package br.com.ilink.pontaltechapi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SizeList {

  int min() default 1;

  int max() default -1;

  String message() default "Quantidade mínima da lista é {min} e máxima de {max} itens, informou [{cur}].";

}
