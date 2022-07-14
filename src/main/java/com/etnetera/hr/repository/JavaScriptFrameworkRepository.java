package com.etnetera.hr.repository;


import com.etnetera.hr.data.model.JavaScriptFramework;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring data repository interface used for accessing the data in database.
 *
 * @author Etnetera
 *
 */
public interface JavaScriptFrameworkRepository extends JpaRepository<JavaScriptFramework, Long> {

    public Optional<JavaScriptFramework> findByName(String name);
}
