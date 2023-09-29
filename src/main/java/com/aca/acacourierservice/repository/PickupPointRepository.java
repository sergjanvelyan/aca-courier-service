package com.aca.acacourierservice.repository;

import com.aca.acacourierservice.entity.PickupPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PickupPointRepository extends JpaRepository<PickupPoint, Long> {
    Page<PickupPoint> findAllByStoreId(long storeId, Pageable pageable);
    boolean existsByIdAndStore_Admin_Email(long id,String email);
    Optional<PickupPoint> findByIdAndStore_Admin_Email(long id,String email);
}