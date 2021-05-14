package com.tt.shopping.common.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class HasHrefName extends HasHref {

    @NotBlank
    @Size(max=1000)
    @Indexed(name = "object_name_index")
    private String name;
}
