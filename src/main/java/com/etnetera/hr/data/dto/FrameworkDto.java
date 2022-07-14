package com.etnetera.hr.data.dto;

import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO Object for transferring data between BE and FE.
 * Contatains framework information.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrameworkDto {

    private Long id;
    @NotNull(message = "Name can not be empty")
    @Size(min = 1, max = 30, message = "Name max size is 15 characters")
    private String name;
    @NotNull(message = "Hype level can not be empty")
    @Min(1)
    @Max(10)
    private Integer hypeLevel;
    @NotNull(message = "Versions can not be empty")
    private List<FrameworkVersionDto> versions;
}
