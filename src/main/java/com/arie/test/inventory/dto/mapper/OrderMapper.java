package com.arie.test.inventory.dto.mapper;

import org.mapstruct.Mapper;

import com.arie.test.inventory.dto.OrderDTO;
import com.arie.test.inventory.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper extends BaseMapper<OrderDTO, Order> {

}
