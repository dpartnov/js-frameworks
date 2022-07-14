package com.etnetera.hr.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO Object for error messages.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessageDto {
    private String error;
}
