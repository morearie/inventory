package com.arie.test.inventory.service;

import java.util.Optional;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arie.test.inventory.dto.OrderDTO;
import com.arie.test.inventory.dto.mapper.OrderMapper;
import com.arie.test.inventory.exception.BadRequestException;
import com.arie.test.inventory.model.Order;
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

	public OrderDTO create(OrderDTO reqDTO) {
		logger.debug("Create order: ", reqDTO);
		return saveOrUpdate(reqDTO);
	}

	public OrderDTO update(long id, OrderDTO reqDTO) {
		logger.debug("update order", reqDTO);
		if(Boolean.TRUE.equals(checkIfExist(id))) {
			reqDTO.setId(id);
			return saveOrUpdate(reqDTO);
		}
		else
			throw new BadRequestException("Order not exist");
	}
	
	@Transactional(readOnly = true)
	public void updateStock(long id, int qty) {
		try {
			productService.updateStockOptimistic( id,  qty);
		} catch (ObjectOptimisticLockingFailureException e) {
			logger.warn("Somebody has already updated the amount for item:{} in concurrent transaction. Will try again...", id);
			productService.updateStockOptimistic( id,  qty);
        }
    }

	@Transactional(readOnly = true)
	public Optional<OrderDTO> getById(long id) {
		logger.debug("Get Order by id: {}", id);
		return orderRepository.findById(id).map(orderMapper::toDto);
	}
	
	private Boolean checkIfExist(long id) {
		return orderRepository.existsById(id);
	}

	private OrderDTO saveOrUpdate(OrderDTO reqDTO) {
		Order order = orderMapper.toEntity(reqDTO);
		order = orderRepository.save(order);
		return orderMapper.toDto(order);
	}
}
