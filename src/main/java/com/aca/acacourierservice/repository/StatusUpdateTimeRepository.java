package com.aca.acacourierservice.repository;

import com.aca.acacourierservice.entity.StatusUpdateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusUpdateTimeRepository  extends JpaRepository<StatusUpdateTime, Long> {
}
