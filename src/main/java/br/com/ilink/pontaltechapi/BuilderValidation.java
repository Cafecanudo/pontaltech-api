package br.com.ilink.pontaltechapi;

import br.com.ilink.pontaltechapi.annotations.DefaultBooleanIfNULL;
import br.com.ilink.pontaltechapi.annotations.DefaultDateIfNULL;
import br.com.ilink.pontaltechapi.annotations.ListNotBlank;
import br.com.ilink.pontaltechapi.annotations.ListOnlyNumber;
import br.com.ilink.pontaltechapi.annotations.NotBlank;
import br.com.ilink.pontaltechapi.annotations.OnlyNumber;
import br.com.ilink.pontaltechapi.annotations.Size;
import br.com.ilink.pontaltechapi.annotations.SizeEachList;
import br.com.ilink.pontaltechapi.annotations.SizeList;
import br.com.ilink.pontaltechapi.exceptions.ValidationException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class BuilderValidation<M> {

  public abstract M build() throws ValidationException;

  protected final M validarParametros(M bean) throws ValidationException {
    Arrays.stream(bean.getClass().getDeclaredFields())
        .forEach(field -> Arrays.stream(field.getAnnotations()).forEach(annotation -> {
          validar(bean, getValue(bean, field.getName()), field, annotation);
        }));
    return bean;
  }

  private Object getValue(M bean, String fieldName) {
    try {
      Field field = getField(bean, fieldName);
      return field.get(bean);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      throw new ValidationException(e);
    }
  }

  private void setValue(M bean, String fieldName, Object value) {
    try {
      Field field = getField(bean, fieldName);
      field.set(bean, value);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      throw new ValidationException(e);
    }
  }

  private void validar(M bean, Object value, Field field, Annotation annotation) {
    //Setters
    if (annotation.annotationType().equals(DefaultBooleanIfNULL.class)) {
      if (value == null) {
        DefaultBooleanIfNULL ann = (DefaultBooleanIfNULL) annotation;
        setValue(bean, field.getName(), ann.value());
      }
    }
    if (annotation.annotationType().equals(DefaultDateIfNULL.class)) {
      if (value == null) {
        setValue(bean, field.getName(), LocalDateTime.now());
      }
    }

    //Validadores
    if (annotation.annotationType().equals(NotBlank.class)) {
      validarVazio(value, field, (NotBlank) annotation);
    }

    if (annotation.annotationType().equals(SizeList.class)) {
      verificarTamanhoLista((Set) value, field, (SizeList) annotation);
    }

    if (annotation.annotationType().equals(ListOnlyNumber.class)) {
      verificarItemListaSomenteNumero((HashSet) value, field, (ListOnlyNumber) annotation);
    }

    if (annotation.annotationType().equals(ListNotBlank.class)) {
      verificarSeItensSaoVazio((HashSet) value, field, (ListNotBlank) annotation);
    }

    if (annotation.annotationType().equals(SizeEachList.class)) {
      verificarTamanhoCadaItemLista((HashSet) value, field, (SizeEachList) annotation);
    }

    if (annotation.annotationType().equals(Size.class)) {
      validarTamando((String) value, field, (Size) annotation);
    }
    if (annotation.annotationType().equals(OnlyNumber.class)) {
      validarSomenteNumeros((String) value, field, (OnlyNumber) annotation);
    }
  }

  private void verificarTamanhoCadaItemLista(HashSet value, Field field, SizeEachList annotation) {
    SizeEachList size = annotation;
    value.stream().forEach(phone -> {
      if (phone != null && (phone.toString().length() < size.min()
          || phone.toString().length() > size.max())) {
        throw new ValidationException(field.getName(),
            size.message()
                .replaceAll("\\{cur\\}", String.valueOf(phone.toString().length()))
                .replaceAll("\\{min\\}", String.valueOf(size.min()))
                .replaceAll("\\{max\\}",
                    String.valueOf(size.max() > 0 ? size.max() : "[SEM LIMITES]"))
        );
      }
    });
  }

  private void verificarSeItensSaoVazio(HashSet value, Field field, ListNotBlank annotation) {
    ListNotBlank listNotBlank = annotation;
    value.stream().forEach(_value -> {
      if (_value == null
          || (field.getType().equals(String.class) && _value.toString().isEmpty())) {
        throw new ValidationException(field.getName(), listNotBlank.message());
      }
    });
  }

  private void verificarItemListaSomenteNumero(HashSet value, Field field,
      ListOnlyNumber annotation) {
    ListOnlyNumber listOnlyNumber = annotation;
    value.stream().forEach(_value -> {
      if (_value != null && !_value.toString().isEmpty() && !_value.toString()
          .matches("[0-9]+")) {
        throw new ValidationException(field.getName(), listOnlyNumber.message());
      }
    });
  }

  private void verificarTamanhoLista(Set value, Field field, SizeList annotation) {
    SizeList size = annotation;
    Set list = value;
    if (list == null || list.isEmpty()) {
      throw new ValidationException(field.getName(),
          size.message()
              .replaceAll("\\{cur\\}", String.valueOf(list.size()))
              .replaceAll("\\{min\\}", String.valueOf(size.min()))
              .replaceAll("\\{max\\}",
                  String.valueOf(size.max() > 0 ? size.max() : "[SEM LIMITES]"))
      );
    }
  }

  private void validarSomenteNumeros(String value, Field field, OnlyNumber annotation) {
    OnlyNumber notBlank = annotation;
    if (value != null && !value.isEmpty() && !value.matches("[0-9]+")) {
      throw new ValidationException(field.getName(), notBlank.message());
    }
  }

  private void validarVazio(Object value, Field field, NotBlank annotation) {
    NotBlank notBlank = annotation;
    if (value == null
        || (field.getType().equals(String.class) && value.toString().isEmpty())) {
      throw new ValidationException(field.getName(), notBlank.message());
    }
  }

  private void validarTamando(String value, Field field, Size annotation) {
    Size size = annotation;
    String _value = value;
    if (_value != null && (_value.length() < size.min() || _value.length() > size.max())) {
      throw new ValidationException(field.getName(),
          size.message()
              .replaceAll("\\{cur\\}", String.valueOf(_value.length()))
              .replaceAll("\\{min\\}", String.valueOf(size.min()))
              .replaceAll("\\{max\\}", String.valueOf(size.max()))
      );
    }
  }

  private Field getField(M bean, String fieldName) throws NoSuchFieldException {
    Field field = bean.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field;
  }
}
