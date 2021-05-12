package com.tt.shopping.api.model.product;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.tt.shopping.api.model.Characteristic;
import com.tt.shopping.api.model.HasHrefName;
import com.tt.shopping.api.model.product.constants.ProductOfferingStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Akshay Misra on 09-08-2018.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Document(collection="products")
public class Product extends HasHrefName {

    @NotBlank
    private String sku;

    @Indexed(name = "product_desc_index")
    private String description;

    private String imageUrl;

    private ProductOfferingStatus status;

    @NotNull
    private int quantityPerUser;

    @NotNull
    private LocalDateTime launchDate;

    private Price price;

    private List<Characteristic> characteristic;
}
