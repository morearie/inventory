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
import com.arie.test.inventory.dto.OrderDTO;
import com.arie.test.inventory.dto.OrderReqDTO;
import com.arie.test.inventory.service.OrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrderController {

	private final OrderService orderService;

	@PostMapping()
	public ResponseEntity<GeneralWrapper<OrderDTO>> addOrder(@RequestBody OrderReqDTO request)
			throws URISyntaxException {
		OrderDTO result = orderService.create(request);
		return ResponseEntity.created(new URI("/api/order/" + result.getId()))
				.body(new GeneralWrapper<OrderDTO>().success(HttpStatus.CREATED, result));
	}

	@PutMapping("/{id}")
	public ResponseEntity<GeneralWrapper<OrderDTO>> updateOrder(@PathVariable long id,
			@RequestBody OrderReqDTO request) throws URISyntaxException {
		OrderDTO result = orderService.update(id, request);
		return ResponseEntity.created(new URI("/api/order/" + result.getId()))
				.body(new GeneralWrapper<OrderDTO>().success(HttpStatus.OK, result));
	}

	@GetMapping("/{id}")
	public ResponseEntity<GeneralWrapper<OrderDTO>> getOrderById(@PathVariable long id) {
		OrderDTO result = orderService.getById(id);
		return ResponseEntity.ok().body(new GeneralWrapper<OrderDTO>().success(HttpStatus.OK, result));
	}

	@GetMapping("/all")
	public ResponseEntity<GeneralWrapper<Page<OrderDTO>>> getOrders(Pageable pageable) {
		Page<OrderDTO> results = orderService.getPage(pageable);
		return ResponseEntity.ok().body(new GeneralWrapper<Page<OrderDTO>>().success(HttpStatus.OK, results));
	}

}
