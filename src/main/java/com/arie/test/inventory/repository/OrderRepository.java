package com.arie.test.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arie.test.inventory.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
