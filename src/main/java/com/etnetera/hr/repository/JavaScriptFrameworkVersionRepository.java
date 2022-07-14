package com.etnetera.hr.repository;

import com.etnetera.hr.data.model.JavaScriptFramework;
import com.etnetera.hr.data.model.JavaScriptFrameworkVersion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JavaScriptFrameworkVersionRepository extends JpaRepository<JavaScriptFrameworkVersion, Long>{
    
    public List<JavaScriptFrameworkVersion> findByFramework(JavaScriptFramework framework);
}
