package com.arie.test.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arie.test.inventory.model.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long>{

}
