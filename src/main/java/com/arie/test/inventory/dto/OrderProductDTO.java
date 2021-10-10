package com.arie.test.inventory.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3097234958842205120L;
	
	@ApiModelProperty(hidden = true)
	private long id;
	private int quantity;
	private float totalPrice;
	private ProductDTO productDTO;
}
