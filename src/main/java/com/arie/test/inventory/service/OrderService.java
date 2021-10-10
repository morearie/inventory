package com.arie.test.inventory.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arie.test.inventory.constant.BadReqConstant;
import com.arie.test.inventory.dto.OrderDTO;
import com.arie.test.inventory.dto.OrderProductReqDTO;
import com.arie.test.inventory.dto.OrderReqDTO;
import com.arie.test.inventory.dto.mapper.OrderMapper;
import com.arie.test.inventory.dto.mapper.OrderProductMapper;
import com.arie.test.inventory.exception.BadRequestException;
import com.arie.test.inventory.model.Order;
import com.arie.test.inventory.model.OrderProduct;
import com.arie.test.inventory.repository.OrderProductRepository;
import com.arie.test.inventory.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderMapper orderMapper;
	private final ProductService productService;
	private final OrderProductRepository orderProductRepository;
	private final OrderProductMapper orderProductMapper;

	public OrderDTO create(OrderReqDTO reqDTO) {
		logger.debug("Create order: ", reqDTO);
		return saveOrUpdate(reqDTO);
	}

	public OrderDTO update(long id, OrderReqDTO reqDTO) {
		logger.debug("update order", reqDTO);
		if (Boolean.TRUE.equals(checkIfExist(id))) {
			reqDTO.setId(id);
			return saveOrUpdate(reqDTO);
		} else
			throw new BadRequestException("Order not exist");
	}

	@Transactional(readOnly = true)
	@Retryable(value = { Exception.class }, maxAttempts = 2, backoff = @Backoff(delay = 200))
	public void updateStock(long id, int qty) {
		productService.updateStockOptimistic(id, qty);
	}

	@Recover
	public void updateStockFallback(Exception e) {
		logger.debug("All retries completed, calling fallback");
		throw new BadRequestException(BadReqConstant.PRODUCT_STOCK);
	}

	@Transactional(readOnly = true)
	public OrderDTO getById(long id) {
		logger.debug("Get Order by id: {}", id);
		Order order = orderRepository.findById(id).get();
		for(OrderProduct pd:order.getOrderProducts()) {
			logger.debug("test::{}",pd.getProduct().getDescription());
		}
		return orderRepository.findById(id).map(orderMapper::toDto)
				.orElseThrow(() -> new BadRequestException(BadReqConstant.ORDER_NOT_EXIST));
	}
	
	@Transactional(readOnly = true)
	public Page<OrderDTO> getPage(Pageable pageable) {
		logger.debug("Get all orders page");
		return orderRepository.findAll(pageable).map(orderMapper::toDto);
	}

	private Boolean checkIfExist(long id) {
		return orderRepository.existsById(id);
	}

	private OrderDTO saveOrUpdate(OrderReqDTO reqDTO) {
		Order order = orderMapper.toEntityReq(reqDTO);
		order = orderRepository.save(order);
		for (OrderProductReqDTO dto : reqDTO.getOrderProducts()) {
			OrderProduct op = orderProductMapper.toEntityReq(dto);
			op.setOrder(order);
			op.setProduct(productService.getEntityById(dto.getProductId()));
			orderProductRepository.save(op);
		}
		return orderMapper.toDto(order);
	}
}
