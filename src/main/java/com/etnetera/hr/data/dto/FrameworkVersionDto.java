package com.etnetera.hr.data.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrameworkVersionDto {
    @NotNull(message = "Version can not be empty")
    @Size(min = 1, max = 15, message = "Version max size is 15 characters")
    private String version;
    @NotNull(message = "Deprecation date can not be empty")
    private LocalDate deprecationDate;
}
