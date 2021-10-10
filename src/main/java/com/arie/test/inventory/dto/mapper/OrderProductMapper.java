package com.arie.test.inventory.dto.mapper;

import org.mapstruct.Mapper;

import com.arie.test.inventory.dto.OrderProductDTO;
import com.arie.test.inventory.model.OrderProduct;

@Mapper(componentModel = "spring")
public interface OrderProductMapper extends BaseMapper<OrderProductDTO, OrderProduct> {

}
