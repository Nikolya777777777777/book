package com.example.demo.dto.order;

import com.example.demo.model.enums.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusRequestDto {
    @NotBlank
    private Status status;
}
