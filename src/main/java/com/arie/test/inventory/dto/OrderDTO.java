package com.arie.test.inventory.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3097234958842205120L;
	
	@ApiModelProperty(hidden = true)
	private long id;
	private long userId;
	private float total;
	private boolean status;
	@JsonInclude(value = Include.NON_EMPTY, content = Include.NON_NULL)
	private List<OrderProductDTO> orderProducts = new ArrayList<>();
}
