package com.tt.shopping.product.impl.utils;

import com.tt.shopping.common.api.model.Characteristic;
import com.tt.shopping.product.api.model.Price;
import com.tt.shopping.product.api.model.Product;
import com.tt.shopping.product.api.model.constants.PriceType;
import com.tt.shopping.product.api.model.constants.ProductOfferingStatus;
import com.tt.shopping.product.api.model.constants.RecurringChargePeriod;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class SampleData {

    Price ONE_TIME =
            Price.builder()
                    .name("One Time Charge")
                    .description("Amount charge only once from customer.")
                    .priceType(PriceType.ONE_TIME)
                    .basePrice(20.22)
                    .taxRate(18.05)
                    .build();


    Price RECURRING =
            Price.builder()
                    .name("One Time Charge")
                    .description("Amount charge only once from customer.")
                    .priceType(PriceType.RECURRING)
                    .recurringChargePeriod(RecurringChargePeriod.MONTHLY)
                    .basePrice(20.22)
                    .taxRate(18.05)
                    .build();


    List<Characteristic> SCI_FI = Arrays.asList(
            new Characteristic("author", "Akshay Misra"),
            new Characteristic("publication", "Xyz Publications"),
            new Characteristic("genre", "Sci-Fi")
    );

    List<Characteristic> HISTORY = Arrays.asList(
            new Characteristic("author", "Akshay Misra"),
            new Characteristic("publication", "ABC Publications"),
            new Characteristic("genre", "History")
    );

    List<Characteristic> ROMANCE = Arrays.asList(
            new Characteristic("author", "Akshay Misra"),
            new Characteristic("publication", "BB Publications"),
            new Characteristic("genre", "Romance")
    );

    List<Product> PRODUCTS = Arrays.asList(
            Product.builder()
                    .name("Book Sci-Fi 1")
                    .description("A book based on science fiction.")
                    .sku("BKSCIFI01")
                    .price(ONE_TIME)
                    .quantityPerUser(3)
                    .status(ProductOfferingStatus.ACTIVE)
                    .launchDate(LocalDateTime.now().minusDays(1L))
                    .characteristic(SCI_FI)
                    .build(),
            Product.builder()
                    .name("Book Historical 1")
                    .description("A book based on historical events.")
                    .sku("BKHIST01")
                    .price(ONE_TIME)
                    .quantityPerUser(2)
                    .status(ProductOfferingStatus.ACTIVE)
                    .launchDate(LocalDateTime.now().minusDays(1L))
                    .characteristic(HISTORY)
                    .build(),
            Product.builder()
                    .name("Book Romance 1")
                    .description("A book based on romantic stories.")
                    .sku("BKROMT01")
                    .price(ONE_TIME)
                    .quantityPerUser(1)
                    .status(ProductOfferingStatus.ACTIVE)
                    .launchDate(LocalDateTime.now().minusDays(1L))
                    .characteristic(ROMANCE)
                    .build(),
            Product.builder()
                    .name("Book Sci-Fi 2")
                    .description("A book based on science fiction 2.")
                    .sku("BKSCIFI02")
                    .price(ONE_TIME)
                    .quantityPerUser(3)
                    .status(ProductOfferingStatus.ACTIVE)
                    .launchDate(LocalDateTime.now().plusMonths(1L))
                    .characteristic(SCI_FI)
                    .build(),
            Product.builder()
                    .name("Book Historical 2")
                    .description("A book based on historical events 2.")
                    .sku("BKHIST02")
                    .price(ONE_TIME)
                    .quantityPerUser(5)
                    .status(ProductOfferingStatus.ACTIVE)
                    .launchDate(LocalDateTime.now().minusDays(1L))
                    .characteristic(HISTORY)
                    .build(),
            Product.builder()
                    .name("Book Romance 2")
                    .description("A book based on romantic stories 2.")
                    .sku("BKROMT02")
                    .price(ONE_TIME)
                    .quantityPerUser(2)
                    .status(ProductOfferingStatus.ACTIVE)
                    .launchDate(LocalDateTime.now().minusDays(1L))
                    .characteristic(ROMANCE)
                    .build()
    );

    public List<Product> getSampleProducts() {
        return this.PRODUCTS;
    }

}
