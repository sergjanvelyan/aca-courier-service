package com.aca.acacourierservice.repository;

import com.aca.acacourierservice.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page <Order> findAllByStoreId (long storeId, Pageable pageable);
    Page <Order> findAllByCourierIsNull(Pageable pageable);
    Page <Order> findAllByCourierId(long courierId,Pageable pageable);
    Page<Order> findAll (Specification<Order> specification,Pageable pageable);
}