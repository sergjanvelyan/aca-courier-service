package com.aca.acacourierservice.service;

import com.aca.acacourierservice.repository.PickupPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PickupPointService {
    private final PickupPointRepository pickupPointRepository;

    @Autowired
    public PickupPointService(PickupPointRepository pickupPointRepository) {
        this.pickupPointRepository = pickupPointRepository;
    }
}
