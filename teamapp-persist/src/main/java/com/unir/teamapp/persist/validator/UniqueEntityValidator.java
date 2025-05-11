package com.unir.teamapp.persist.validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.stereotype.Component;

import com.unir.teamapp.persist.annotation.UniqueEntity;
import com.unir.teamapp.persist.entity.BaseEntity;
import com.unir.teamapp.persist.repository.common.ComplexJpaRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class UniqueEntityValidator implements ConstraintValidator<UniqueEntity, BaseEntity> {

    private static final String GET_ID = "getId";

    private final ApplicationContext applicationContext;
   
    private Set<String> fields;
    
    /** 
     * @param entity
     * @param context
     * @return boolean
     */
    @Override
    public boolean isValid(final BaseEntity entity, final ConstraintValidatorContext context) {
        final List<Boolean> validationResults = fields.stream().map(field -> validateField(entity, field, context)).toList();

        return validationResults.stream().allMatch(Boolean::booleanValue);
    }

    
    /** 
     * @param entity
     * @param field
     * @param context
     * @return boolean
     */
    @SuppressWarnings({ "unchecked"})
    private boolean validateField(final BaseEntity entity, final String field, final ConstraintValidatorContext context) {
        try {
            final var ignoredFieldNames = getIgnoredFieldNames(entity, field);

            final var fieldMatcher = createFieldMatcher(field, ignoredFieldNames);

            final var entities = CollectionUtils.isNotEmpty(fieldMatcher.getPropertySpecifiers().getSpecifiers())
                ? getRepositoryByEntity(entity).findAll(Example.of(entity, fieldMatcher)) : new ArrayList<>();

            return entities.isEmpty() || entities.size() == 1 && entity.getClass().getMethod(UniqueEntityValidator.GET_ID).invoke(entity) != null
            && entity.getClass().getMethod(UniqueEntityValidator.GET_ID).invoke(entity).equals(entities.get(0).getClass().getMethod(UniqueEntityValidator.GET_ID).invoke(entities.get(0)));

        } catch (final Exception e) {
            UniqueEntityValidator.log.error("Error validando unicidad de los campos {} de la clase {}", fields, entity.getClass().getSimpleName(), e);
            return true;
        }
    }
        
    /** 
     * @param entity
     * @param field
     * @return Set<String>
     */
    private Set<String> getIgnoredFieldNames(final BaseEntity entity, final String field) {
        return UniqueEntityValidator.getAllFields(entity.getClass()).stream()
            .filter(classField -> !field.equals(classField.getName()) || isFieldValueNull(entity, classField, field))
            .map(Field::getName)
            .collect(Collectors.toSet());
    }

    
    /** 
     * @param entity
     * @param classField
     * @param fieldCheck
     * @return boolean
     */
    private boolean isFieldValueNull(final BaseEntity entity, final Field classField, final String fieldCheck) {
        boolean isNull = false;
        try {
            classField.setAccessible(true);
            isNull = classField.getName().equals(fieldCheck) && classField.get(entity) == null;
        } catch (IllegalArgumentException | IllegalAccessException e){
            UniqueEntityValidator.log.error("Error obteniendo el valor del campo {}", classField.getName(), e);
            isNull = true;
        }
        return isNull;
    }

    
    /** 
     * @param field
     * @param ignoredFieldNames
     * @return ExampleMatcher
     */
    private ExampleMatcher createFieldMatcher(final String field, final Set<String> ignoredFieldNames) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths(ignoredFieldNames.toArray(new String[0]));
        if (!ignoredFieldNames.contains(field)) {
            matcher = matcher.withMatcher(field, GenericPropertyMatcher::exact);
        }

        return matcher;
    }

    
    /** 
     * @param type
     * @return List<Field>
     */
    private static List<Field> getAllFields(final Class<?> type) {
        return UniqueEntityValidator.getAllFields(new ArrayList<>(), type);
    }

    
    /** 
     * @param fields
     * @param type
     * @return List<Field>
     */
    private static List<Field> getAllFields(final List<Field> fields, final Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        if (type.getSuperclass() != null) {
            UniqueEntityValidator.getAllFields(fields, type.getSuperclass());
        }
        return fields;
    }
    
    /** 
     * @param entity
     * @return ComplexJpaRepository
     */
    @SuppressWarnings("rawtypes")
    private ComplexJpaRepository getRepositoryByEntity(final Object entity) {
        return applicationContext.getBeansOfType(ComplexJpaRepository.class).values().stream()
            .filter(repository -> repository.getType().equals(entity.getClass())).findFirst()
            .orElseThrow(()-> new IllegalArgumentException("No se ha encontrado el repositorio de la clase: " + entity.getClass()));
    }
}
