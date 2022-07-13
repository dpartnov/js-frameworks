package com.etnetera.hr.service;

import com.etnetera.hr.data.dto.FrameworkDto;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FrameworkService {

    @Autowired
    private JavaScriptFrameworkRepository repository;

    public List<FrameworkDto> getAll() {
        return repository.findAll().stream().map(entity -> {
            FrameworkDto dto = new FrameworkDto();
            dto.setId(entity.getId());
            dto.setHypeLevel(entity.getHypeLevel());
            dto.setName(entity.getName());
            dto.setDeprecationDate(entity.getDeprecationDate());
            dto.setVersions(entity.getVersions().stream().map(v -> v.getVersion()).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }
}
