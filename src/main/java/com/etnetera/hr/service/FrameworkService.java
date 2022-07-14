package com.etnetera.hr.service;

import com.etnetera.hr.data.dto.FrameworkDto;
import com.etnetera.hr.data.dto.FrameworkVersionDto;
import com.etnetera.hr.data.model.JavaScriptFramework;
import com.etnetera.hr.data.model.JavaScriptFrameworkVersion;
import com.etnetera.hr.exception.ValidationException;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.etnetera.hr.repository.JavaScriptFrameworkVersionRepository;
import java.util.List;
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
        entity.setName(payload.getName());
        entity.setHypeLevel(payload.getHypeLevel());
        entity = repository.save(entity);

        for (FrameworkVersionDto versionDto : payload.getVersions()) {
            JavaScriptFrameworkVersion versionEntity = new JavaScriptFrameworkVersion();
            versionEntity.setVersion(versionDto.getVersion());
            versionEntity.setDeprecationDate(versionDto.getDeprecationDate());
            versionEntity.setFramework(entity);
            versionRepository.save(versionEntity);
        }
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
