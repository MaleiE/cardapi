package com.malei.card.api.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

@Data
@EqualsAndHashCode(callSuper = false)
public class CreateAndUpdateUserDto extends ResourceSupport {
    @JsonProperty("name")
    private String userName;
}
