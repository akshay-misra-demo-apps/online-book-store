package com.tt.shopping.product.impl.service;


import com.tt.shopping.common.api.exceptions.ResourceNotFoundException;
import com.tt.shopping.product1.impl.repositories.ProductRepository;
import com.tt.shopping.product.impl.utils.SampleData;
import com.tt.shopping.product.api.model.Product;
import com.tt.shopping.product.api.service.ProductManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductManagementServiceImpl implements ProductManagementService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SampleData sampleData;

    @Override
    public List<Product> getProducts() {
        final Sort sortByCreatedAtDesc = Sort.by(Sort.Direction.DESC, "createdAt");
        return this.productRepository.findAll(sortByCreatedAtDesc);
    }

    @Override
    public Product getProductById(String id) {
        return this.productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product with id '" +  id + "' not found."));
    }

    @Override
    public Product getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    @Override
    public Product createProduct(Product product) {
        return this.productRepository.save(product);
    }

    @Override
    public void createSampleProduct() {
        if (this.productRepository.findAll().isEmpty()) {
            this.productRepository.saveAll(this.sampleData.getSampleProducts());
        }
    }

    @Override
    public Product updateProduct(Product product) {
        return this.productRepository.save(product);
    }

    @Override
    public boolean deleteProduct(String id) {
        final Product product = this.getProductById(id);
        if(product != null) {
            this.productRepository.deleteById(id);
            return true;
        }

        return false;
    }
}
