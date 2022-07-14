package com.etnetera.hr.data.model;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data entity for FRAMEWORK_VERSIONS table.
 */
@Entity(name = "FRAMEWORK_VERSIONS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JavaScriptFrameworkVersion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FRAMEWORK_ID", nullable = false)
    private JavaScriptFramework framework;
    
    @Column(name = "VERSION", nullable = false, length = 15)
    private String version;
    
    @Column(name = "DEPRECATION_DATE", nullable = false)
    private LocalDate deprecationDate;
}
