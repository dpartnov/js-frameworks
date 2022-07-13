package com.etnetera.hr.data.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data entity for FRAMEWORK_VERSIONS table
 */
@Entity(name = "FRAMEWORK_VERSIONS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JavaScriptFrameworkVersion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Column(name = "FRAMEWORK_ID", nullable = false)
    private Long frameworkId;
    
    @Column(name = "VERSION", nullable = false, length = 15)
    private String version;
}
