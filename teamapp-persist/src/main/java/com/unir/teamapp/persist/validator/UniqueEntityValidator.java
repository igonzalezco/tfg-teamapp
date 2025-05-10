package com.unir.teamapp.persist.validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
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

    private static final String PREFIX_I18N = "entiry-validation.error.unique.";
    private static final String GET_ID = "getId";
    private static final String SET_ID = "setId";
    private static final String GET_ENABLED = "getEnabled";

    private final ApplicationContext applicationContext;
    private final MessageSource messageSource;
    // private final UserService userService;
    private Set<String> fields;
    private boolean hasEnabledField;

    
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

            final boolean validEntity;

            if (!hasEnabledField) {
                validEntity = entities.isEmpty() || isSameEntity(entities, entity);

                if (!validEntity) {
                    buildErrorMessage(field, context);
                }
            } else {
                validEntity = entities.isEmpty() || entities.stream().noneMatch(existingEntity -> isInvalidEntity(existingEntity, entity, field, context));
            }

            return validEntity;
        } catch (final Exception e) {
            UniqueEntityValidator.log.error("Error validating uniqueness of fields {} in class {}", fields, entity.getClass().getSimpleName(), e);
            return true;
        }
    }

    
    /** 
     * @param field
     * @param context
     */
    private void buildErrorMessage(final String field, final ConstraintValidatorContext context) {
        final String errorMessage = getErrorMessage(field, context);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMessage).addPropertyNode(field).addConstraintViolation();
    }

    
    /** 
     * @param field
     * @param context
     * @return String
     */
    private String getErrorMessage(final String field, final ConstraintValidatorContext context) {
        final String messageAnotation = context.getDefaultConstraintMessageTemplate();
        //TODO obtener locale del usuario
        return messageSource.getMessage(UniqueEntityValidator.PREFIX_I18N + field, null, messageAnotation, Locale.ENGLISH);
    }

    
    /** 
     * @param existingEntity
     * @param entity
     * @param field
     * @param context
     * @return boolean
     */
    private boolean isInvalidEntity(final Object existingEntity, final BaseEntity entity, final String field, final ConstraintValidatorContext context) {
        try {
            final Boolean enabled = (Boolean) existingEntity.getClass().getMethod(UniqueEntityValidator.GET_ENABLED).invoke(existingEntity);
            final var entityId = entity.getClass().getMethod(UniqueEntityValidator.GET_ID).invoke(entity);

            if (Boolean.TRUE.equals(enabled)) {
                if (entityId == null) {
                    buildErrorMessage(field, context);
                    return true;
                }

                return false;
            }

            assignEntityId(existingEntity, entity);
            return false;
        } catch (final Exception e) {
            UniqueEntityValidator.log.error("Error validando unicidad", e);
            return false;
        }
    }

    
    /** 
     * @param existingEntity
     * @param entity
     * @throws Exception
     */
    private void assignEntityId(final Object existingEntity, final BaseEntity entity) throws Exception {
        final Object existingEntityId = existingEntity.getClass().getMethod(UniqueEntityValidator.GET_ID).invoke(existingEntity);
        entity.getClass().getMethod(UniqueEntityValidator.SET_ID, existingEntityId.getClass()).invoke(entity, existingEntityId);
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
     * @param entities
     * @param entity
     * @return boolean
     * @throws Exception
     */
    private boolean isSameEntity(final List<BaseEntity> entities, final BaseEntity entity) throws Exception {
        final var entityId = entity.getClass().getMethod(UniqueEntityValidator.GET_ID).invoke(entity);
        final var existingEntityId = entities.get(0).getClass().getMethod(UniqueEntityValidator.GET_ID).invoke(entities.get(0));
        return entities.size() == 1 && entityId != null && entityId.equals(existingEntityId);
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
