package com.aca.acacourierservice.service;

import com.aca.acacourierservice.converter.PickupPointConverter;
import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.exception.CourierServiceException;
import com.aca.acacourierservice.model.PickupPointJson;
import com.aca.acacourierservice.repository.PickupPointRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PickupPointServiceTest {
    @Mock
    private  PickupPointRepository pickupPointRepository;
    @Mock
    private  PickupPointConverter pickupPointConverter;
    @InjectMocks
    private PickupPointService pickupPointService;

    @Test
    public void testGetPickupPointById_ExistingId() {
        long id = 1L;
        PickupPoint pickupPoint = new PickupPoint();
        when(pickupPointRepository.findById(id)).thenReturn(Optional.of(pickupPoint));

        PickupPoint result = pickupPointService.getPickupPointById(id);

        assertNotNull(result);
        assertEquals(pickupPoint, result);
    }

    @Test
    public void testGetPickupPointById_NonExistingId() {
        long id = 1L;
        when(pickupPointRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CourierServiceException.class, () -> pickupPointService.getPickupPointById(id));
    }

    @Test
    public void testAddPickupPoint_ReturnsId() {
        PickupPointJson pickupPointJson = new PickupPointJson();
        PickupPoint pickupPoint = new PickupPoint();
        when(pickupPointConverter.convertToEntity(pickupPointJson)).thenReturn(pickupPoint);
        when(pickupPointRepository.save(pickupPoint)).thenReturn(pickupPoint);

        long resultId = pickupPointService.addPickupPoint(pickupPointJson).getId();

        assertEquals(pickupPoint.getId(), resultId);
    }
}

