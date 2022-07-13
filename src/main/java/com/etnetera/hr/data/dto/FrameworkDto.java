package com.etnetera.hr.data.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO Object for transferring data between BE and FE.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrameworkDto {
    
    private Long id;
    private String name;
    private LocalDate deprecationDate;
    private Integer hypeLevel;
    private List<String> versions;
}
