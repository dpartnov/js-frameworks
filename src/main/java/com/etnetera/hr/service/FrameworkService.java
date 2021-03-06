package com.etnetera.hr.service;

import com.etnetera.hr.data.dto.FrameworkDto;
import com.etnetera.hr.data.dto.FrameworkVersionDto;
import com.etnetera.hr.data.model.JavaScriptFramework;
import com.etnetera.hr.data.model.JavaScriptFrameworkVersion;
import com.etnetera.hr.exception.ValidationException;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.etnetera.hr.repository.JavaScriptFrameworkVersionRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FrameworkService {

    @Autowired
    private JavaScriptFrameworkRepository repository;
    @Autowired
    private JavaScriptFrameworkVersionRepository versionRepository;

    /**
     * Get all frameworks from DB.
     *
     * @return array of {@link FrameworkDto} objects.
     */
    public List<FrameworkDto> getAll() {
        return repository.findAll().stream().map(entity -> {
            return entityToDto(entity);
        }).collect(Collectors.toList());
    }

    /**
     * Get all frameworks from DB by name filter.
     *
     * @param name filter value
     * @return array of {@link FrameworkDto} objects.
     */
    public List<FrameworkDto> getAllByFilter(String name) {
        return repository.findByNameContainingIgnoreCase(name).stream().map(entity -> {
            return entityToDto(entity);
        }).collect(Collectors.toList());
    }

    /**
     * Get detail about framework entity from DB.
     *
     * @param id framwork ID
     * @return {@link FrameworkDto}
     */
    public FrameworkDto getDetail(Long id) {
        return entityToDto(getFrameworkById(id));
    }

    /**
     * Create new framework entity.
     *
     * @throws ValidationException if other framework with same name exists in
     * DB.
     * @param payload {@link FrameworkDto} Data about new framework
     */
    public void createFramework(final FrameworkDto payload) {
        validate(payload);
        
        if (repository.findByName(payload.getName().trim()).isPresent()) {
            throw new ValidationException(String.format("Framework %s already exists!", payload.getName()));
        }

        JavaScriptFramework entity = new JavaScriptFramework();
        entity.setName(payload.getName().trim());
        entity.setHypeLevel(payload.getHypeLevel());
        entity = repository.save(entity);

        for (FrameworkVersionDto versionDto : payload.getVersions()) {
            JavaScriptFrameworkVersion versionEntity = new JavaScriptFrameworkVersion();
            versionEntity.setVersion(versionDto.getVersion().trim());
            versionEntity.setDeprecationDate(versionDto.getDeprecationDate());
            versionEntity.setFramework(entity);
            versionRepository.save(versionEntity);
        }
    }

    /**
     * Update information about framework.
     *
     * @throws ValidationException if other framework with same name exists in
     * DB.
     * @param payload New framework information.
     * @param id Framework ID
     */
    public void updateFramework(final FrameworkDto payload, final Long id) {
        validate(payload);
        JavaScriptFramework entity = getFrameworkById(id);

        if (!repository.findByNameAndIdNotIn(payload.getName(), Arrays.asList(payload.getId())).isEmpty()) {
            throw new ValidationException(String.format("Other framework with name %s already exists!", payload.getName()));
        }

        entity.setName(payload.getName());
        entity.setHypeLevel(payload.getHypeLevel());

        //Find all versions in DB for framework
        List<JavaScriptFrameworkVersion> exsistingVersions = versionRepository.findByFramework(entity);
        //Iterate over all versions from request payload
        payload.getVersions().stream().forEach(versionDto -> {
            Optional<JavaScriptFrameworkVersion> tempVersionOpt = exsistingVersions.stream()
                    .filter(v -> v.getVersion().equals(versionDto.getVersion().trim())).findAny();

            //If version from payload exists in DB, update deprecation date for this version
            if (tempVersionOpt.isPresent()) {
                JavaScriptFrameworkVersion versionToUpdate = tempVersionOpt.get();
                versionToUpdate.setDeprecationDate(versionDto.getDeprecationDate());
                versionRepository.save(versionToUpdate);
            } else {
                //If version from payload does not exists in DB, create new
                JavaScriptFrameworkVersion versionToCreate = new JavaScriptFrameworkVersion();
                versionToCreate.setVersion(versionDto.getVersion());
                versionToCreate.setDeprecationDate(versionDto.getDeprecationDate());
                versionToCreate.setFramework(entity);
                versionRepository.save(versionToCreate);
            }

        });

        //Iterate over all versions from DB
        exsistingVersions.stream().forEach(version -> {
            //If payload does not have version from DB, delete this version
            if (payload.getVersions().stream()
                    .filter(dto -> dto.getVersion().trim().equals(version.getVersion())).findAny().isEmpty()) {
                versionRepository.delete(version);
            }
        });

        repository.save(entity);
    }

    /**
     * Delete framework data from DB.
     *
     * @param id Framework ID
     */
    public void delete(Long id) {
        JavaScriptFramework entity = getFrameworkById(id);
        versionRepository.deleteAll(versionRepository.findByFramework(entity));
        repository.delete(entity);
    }

    private FrameworkDto entityToDto(JavaScriptFramework entity) {
        FrameworkDto dto = new FrameworkDto();
        dto.setId(entity.getId());
        dto.setHypeLevel(entity.getHypeLevel());
        dto.setName(entity.getName());

        dto.setVersions(entity.getVersions().stream().map(versionEntity -> {
            FrameworkVersionDto versionDto = new FrameworkVersionDto();
            versionDto.setDeprecationDate(versionEntity.getDeprecationDate());
            versionDto.setVersion(versionEntity.getVersion());
            return versionDto;
        }).collect(Collectors.toList()));
        return dto;
    }

    private JavaScriptFramework getFrameworkById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ValidationException(String.format("Framework with ID %d does not exists!", id)));
    }

    /**
     * Provides validation check.
     * For some reason base validation by annotation in DTO object does't work.
     * @param payload {@link FrameworkDto} data to be validated
     */
    private void validate(final FrameworkDto payload) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<FrameworkDto>> violations = validator.validate(payload);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
