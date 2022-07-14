package com.etnetera.hr.data.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simple data entity describing basic properties of every JavaScript framework.
 *
 * @author Etnetera
 *
 */
@Entity(name = "FRAMEWORKS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JavaScriptFramework implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 30)
    private String name;

    @Column(name = "DEPRECATION_DATE", nullable = false)
    private LocalDate deprecationDate;

    @Min(1)
    @Max(10)
    @Column(name = "HYPE_LEVEL", nullable = false)
    private Integer hypeLevel;
    
    @OneToMany(mappedBy="frameworkId")
    private List<JavaScriptFrameworkVersion> versions;
    
    @Override
    public String toString() {
        return id.toString();
    }
}