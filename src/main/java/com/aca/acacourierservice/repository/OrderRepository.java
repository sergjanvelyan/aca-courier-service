package com.aca.acacourierservice.repository;

import com.aca.acacourierservice.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByTrackingId (String trackingId);
    Page <Order> findAllByStoreId (long storeId, Pageable pageable);
    Page <Order> findAllByFullName (String customerFullName, Pageable pageable);
    Page <Order> findAllByStatus (Order.Status status, Pageable pageable);
    Page <Order> findAllByStoreIdAndStatus (long storeId,Order.Status status, Pageable pageable);
    Page <Order> findAllByCourierEmpty(Pageable pageable);
}