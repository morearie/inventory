package com.arie.test.inventory.dto.mapper;

import org.mapstruct.Mapper;

import com.arie.test.inventory.dto.ProductDTO;
import com.arie.test.inventory.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper extends BaseMapper<ProductDTO, Product> {
	
	default Product toEntityUpdate(Product product, ProductDTO productDTO) {
		product.setPrice(productDTO.getPrice());
		product.setStock(productDTO.getStock());
		product.setTitle(product.getTitle());
		product.setDescription(productDTO.getDescription());
		return product;
	}
	
	default Product fromId(Long id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }

}
