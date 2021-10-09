package com.arie.test.inventory.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;

import com.arie.test.inventory.constant.ResponseConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class GeneralWrapper<T> {
	private String status;
	private HttpStatus httpStatus;
	private String message;
	@JsonInclude(Include.ALWAYS)
	private T data;
	private Date timestamp = new Date();
	private String signature;
	@JsonIgnore
	private Logger logger = this.getLogger();

	public GeneralWrapper<T> success(HttpStatus httpStatus) {
		this.status = "00";
		this.httpStatus = httpStatus;
		this.message = ResponseConstant.SUCCESS;
		return this;
	}

	public GeneralWrapper<T> success( T t) {
		return this.success(HttpStatus.OK, t, null);
	}
	
	public GeneralWrapper<T> success(HttpStatus httpStatus, T t) {
		return this.success(httpStatus, t, null);
	}

	public GeneralWrapper<T> success(HttpStatus httpStatus, T t, String signature) {
		this.status = "01";
		this.httpStatus = httpStatus;
		this.message = ResponseConstant.SUCCESS;
		this.data = t;
		this.signature = signature;
		return this;
	}

	public GeneralWrapper<T> fail(HttpStatus httpStatus) {
		this.status = "02";
		this.httpStatus = httpStatus;
		this.message = ResponseConstant.FAIL;
		return this;
	}
	
	public GeneralWrapper<T> fail(HttpStatus httpStatus,T t) {
		this.status = "02";
		this.httpStatus = httpStatus;
		this.message = ResponseConstant.FAIL;
		this.data = t;
		return this;
	}

	public GeneralWrapper<T> fail(HttpStatus httpStatus, String message) {
		this.status = "03";
		this.httpStatus = httpStatus;
		this.message = message.isEmpty() ? ResponseConstant.FAIL : message;
		return this;
	}
	
	public GeneralWrapper<T> notFound(HttpStatus httpStatus) {
		this.status = "02";
		this.httpStatus = httpStatus;
		return this;
	}

}
