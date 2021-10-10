package com.arie.test.inventory.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.arie.test.inventory.dto.OrderDTO;
import com.arie.test.inventory.dto.OrderReqDTO;
import com.arie.test.inventory.model.Order;

@Mapper(componentModel = "spring", uses = {OrderProductMapper.class})
public interface OrderMapper extends BaseMapper<OrderDTO, Order> {
	Order toEntityReq (OrderReqDTO orderReqDTO);
	
	@Mapping(target = "orderProducts", source = "orderProducts")
	OrderDTO toDto (Order order);
}
