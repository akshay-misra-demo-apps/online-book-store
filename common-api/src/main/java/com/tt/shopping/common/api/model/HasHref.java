package com.tt.shopping.common.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class HasHref {

    @Id
    @Indexed(name = "object_id_index")
    private String id;

    private String href;
}
