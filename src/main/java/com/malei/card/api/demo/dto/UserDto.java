package com.malei.card.api.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserDto extends ResourceSupport {
    @JsonProperty("id")
    private Long userId;
    @JsonProperty("name")
    private String userName;
}
