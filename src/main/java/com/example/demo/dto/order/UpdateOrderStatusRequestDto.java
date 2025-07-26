package com.example.demo.dto.order;

import com.example.demo.model.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusRequestDto {
    private Status status;
}
