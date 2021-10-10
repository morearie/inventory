package com.arie.test.inventory.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.arie.test.inventory.constant.BadReqConstant;
import com.arie.test.inventory.dto.ProductDTO;
import com.arie.test.inventory.dto.mapper.ProductMapper;
import com.arie.test.inventory.exception.BadRequestException;
import com.arie.test.inventory.model.Product;
import com.arie.test.inventory.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductMapper productMapper;

	public ProductDTO create(ProductDTO reqDTO) {
		logger.debug("Create product: ", reqDTO);
		reqDTO.setStatus(true);
		return save(reqDTO);
	}

	public ProductDTO update(long id, ProductDTO reqDTO) {
		logger.debug("update product", reqDTO);
		if (Boolean.TRUE.equals(checkIfExist(id))) {
			reqDTO.setId(id);
			return update(reqDTO);
		} else
			throw new BadRequestException(BadReqConstant.PRODUCT_NOT_EXIST);
	}

	@Transactional(readOnly = true)
	public ProductDTO getById(long id) {
		logger.debug("Get Product by id: {}", id);
		return productRepository.findById(id).map(productMapper::toDto)
				.orElseThrow(() -> new BadRequestException(BadReqConstant.PRODUCT_NOT_EXIST));
	}

	@Transactional(readOnly = true)
	public Page<ProductDTO> getPage(Pageable pageable) {
		logger.debug("Get all product page");
		return productRepository.findAll(pageable).map(productMapper::toDto);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateStockOptimistic(long id, int qty) {
		logger.debug("update stock product optimistic", id);
		Product product = findById(id);
		int stock = product.getStock();
		stock = stock + qty;
		if (stock <= 0)
			throw new BadRequestException(BadReqConstant.PRODUCT_STOCK);
		product.setStock(stock);
	}

	private Boolean checkIfExist(long id) {
		logger.debug("check product if exist", id);
		return productRepository.existsById(id);
	}

	private ProductDTO save(ProductDTO reqDTO) {
		logger.debug("save product", reqDTO.toString());
		Product product = productMapper.toEntity(reqDTO);
		product = productRepository.save(product);
		return productMapper.toDto(product);
	}

	private ProductDTO update(ProductDTO reqDTO) {
		logger.debug("update product", reqDTO.toString());
		Product product = findById(reqDTO.getId());
		product = productMapper.toEntityUpdate(product, reqDTO);
		return productMapper.toDto(product);
	}

	private Product findById(long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new BadRequestException(BadReqConstant.PRODUCT_NOT_EXIST));
	}
}
