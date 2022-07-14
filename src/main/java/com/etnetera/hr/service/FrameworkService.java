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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FrameworkService {

    @Autowired
    private JavaScriptFrameworkRepository repository;
    @Autowired
    private JavaScriptFrameworkVersionRepository versionRepository;

    public List<FrameworkDto> getAll() {
        return repository.findAll().stream().map(entity -> {
            return entityToDto(entity);
        }).collect(Collectors.toList());
    }

    public FrameworkDto getDetail(Long id) {
        JavaScriptFramework entity = repository.findById(id)
                .orElseThrow(() -> new ValidationException(String.format("Framework with ID %d does not exists!", id)));
        return entityToDto(entity);
    }

    public void createFramework(final FrameworkDto payload) {
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

    public void updateFramework(final FrameworkDto payload, final Long id) {
        JavaScriptFramework entity = repository.findById(id)
                .orElseThrow(() -> new ValidationException(String.format("Framework with ID %d does not exists!", id)));

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
}
