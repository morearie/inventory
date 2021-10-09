package com.arie.test.inventory.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BaseVersionEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1234614398953255553L;

	@Version
	private Integer version;

	@CreatedBy
	@JsonIgnore
	@Column(updatable = false)
	private String createdBy;

	/** The created date. */
	@CreatedDate
	@JsonIgnore
	@Column(updatable = false)
	private Date createdDate = new Date();

	@LastModifiedBy
	@JsonIgnore
	private String lastModifiedBy;

	/** The modified date. */
	@LastModifiedDate
	@JsonIgnore
	private Date lastModifiedDate = new Date();
}
