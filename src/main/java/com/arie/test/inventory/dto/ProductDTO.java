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
public class ProductDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4386816336906819964L;
	
	@ApiModelProperty(hidden = true)
	private long id;
	private String title;
	private String description;
	private float price;
	private int stock;
	@ApiModelProperty(hidden = true)
	private boolean status;
	
	@Override
	public String toString() {
		return "ProductDTO [id=" + id + ", title=" + title + ", description=" + description + ", price=" + price
				+ ", stock=" + stock + ", status=" + status + "]";
	}
	
}
