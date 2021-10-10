package com.arie.test.inventory.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arie.test.inventory.dto.GeneralWrapper;
import com.arie.test.inventory.dto.ProductDTO;
import com.arie.test.inventory.service.OrderService;
import com.arie.test.inventory.service.ProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ProductController {

	private final ProductService productService;
	private final OrderService orderService;

	@PostMapping()
	public ResponseEntity<GeneralWrapper<ProductDTO>> addProduct(@RequestBody ProductDTO request)
			throws URISyntaxException {
		ProductDTO result = productService.create(request);
		return ResponseEntity.created(new URI("/api/product/" + result.getId()))
				.body(new GeneralWrapper<ProductDTO>().success(HttpStatus.CREATED, result));
	}

	@PutMapping("/{id}")
	public ResponseEntity<GeneralWrapper<ProductDTO>> updateProduct(@PathVariable long id,
			@RequestBody ProductDTO request) throws URISyntaxException {
		ProductDTO result = productService.update(id, request);
		return ResponseEntity.created(new URI("/api/product/" + result.getId()))
				.body(new GeneralWrapper<ProductDTO>().success(HttpStatus.OK, result));
	}

	@GetMapping("/{id}")
	public ResponseEntity<GeneralWrapper<ProductDTO>> getProductById(@PathVariable long id) {
		ProductDTO result = productService.getById(id);
		return ResponseEntity.ok().body(new GeneralWrapper<ProductDTO>().success(HttpStatus.OK, result));
	}

	@GetMapping("/all")
	public ResponseEntity<GeneralWrapper<Page<ProductDTO>>> getProducts(Pageable pageable) {
		Page<ProductDTO> results = productService.getPage(pageable);
		return ResponseEntity.ok().body(new GeneralWrapper<Page<ProductDTO>>().success(HttpStatus.OK, results));
	}

	@PutMapping("/{id}/increase-stock/{qty}")
	public ResponseEntity<GeneralWrapper<ProductDTO>> increaseStockProduct(@PathVariable long id,
			@PathVariable int qty) {
		orderService.updateStock(id, qty);
		return ResponseEntity.ok().body(new GeneralWrapper<ProductDTO>().success(HttpStatus.OK));
	}

	@PutMapping("/{id}/decrease-stock/{qty}")
	public ResponseEntity<GeneralWrapper<ProductDTO>> decreaseStockProduct(@PathVariable long id,
			@PathVariable int qty) {
		orderService.updateStock(id, -qty);
		return ResponseEntity.ok().body(new GeneralWrapper<ProductDTO>().success(HttpStatus.OK));
	}

}
