package com.arie.test.inventory;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.arie.test.inventory.constant.BadReqConstant;
import com.arie.test.inventory.dto.ProductDTO;
import com.arie.test.inventory.exception.BadRequestException;
import com.arie.test.inventory.model.Product;
import com.arie.test.inventory.repository.ProductRepository;
import com.arie.test.inventory.service.OrderService;
import com.arie.test.inventory.service.ProductService;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class InventoryApplicationTests {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductService productService;

	@Autowired
	ProductRepository productRepository;

	private final List<Integer> stockQtys = Arrays.asList(-5, -3);

	private final List<Integer> stockQty2s = Arrays.asList(-5, -3, -10);

	@Test
	@Order(1)
	@DisplayName("test updating stock by normal flow not concurrently")
	void testStockProductService_normal() {
		// given
		ProductDTO dto = new ProductDTO();
		dto.setTitle("product1");
		dto.setDescription("testing product 1");
		dto.setPrice(10000);
		dto.setStock(10);
		dto = productService.create(dto);

		// when
		for (final int sq : stockQtys) {
			orderService.updateStock(dto.getId(), sq);
		}

		// then
		final Product product = productRepository.findById(dto.getId()).get();

		assertAll(() -> assertEquals(2, product.getVersion()), () -> assertEquals(2, product.getStock()));
	}

	@Test
	@Order(2)
	@DisplayName("test updating stock by concurrently")
	void testStockProductService_concurrent() throws InterruptedException {
		// given
		ProductDTO dto = new ProductDTO();
		dto.setTitle("product2");
		dto.setDescription("testing product 2");
		dto.setPrice(20000);
		dto.setStock(15);
		dto = productService.create(dto);
		final long id = dto.getId();

		// when
		final ExecutorService executor = Executors.newFixedThreadPool(stockQtys.size());
		for (final int sq : stockQtys) {
			executor.execute(() -> orderService.updateStock(id, sq));
		}

		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.MINUTES);

		// then
		final Product product = productRepository.findById(dto.getId()).get();

		assertAll(() -> assertEquals(2, product.getVersion()), () -> assertEquals(7, product.getStock()));
	}

	@Test
	@Order(3)
	@DisplayName("test updating stock by concurrently with out of stock")
	void testStockProductService_concurrent_oos() throws InterruptedException {
		try {
			// given
			ProductDTO dto = new ProductDTO();
			dto.setTitle("product3");
			dto.setDescription("testing product 3");
			dto.setPrice(30000);
			dto.setStock(15);
			dto = productService.create(dto);
			final long id = dto.getId();

			// when
			final ExecutorService executor = Executors.newFixedThreadPool(stockQty2s.size());
			for (final int sq : stockQty2s) {
				executor.execute(() -> orderService.updateStock(id, sq));
			}

			executor.shutdown();
			executor.awaitTermination(1, TimeUnit.MINUTES);
		} catch (BadRequestException e) {
			// then
			final String expected = BadReqConstant.PRODUCT_STOCK;
			assertEquals(expected, e.getMessage());
		}

	}

}
