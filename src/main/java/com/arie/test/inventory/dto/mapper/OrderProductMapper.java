package com.arie.test.inventory.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.arie.test.inventory.dto.OrderProductDTO;
import com.arie.test.inventory.dto.OrderProductReqDTO;
import com.arie.test.inventory.model.OrderProduct;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderProductMapper extends BaseMapper<OrderProductDTO, OrderProduct> {
	OrderProduct toEntityReq (OrderProductReqDTO orderProductReqDTO);
	
	@Mapping(target = "productDTO", source = "product")
	OrderProductDTO toDto (OrderProduct orderProduct);
}
