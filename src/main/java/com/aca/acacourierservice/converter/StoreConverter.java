package com.aca.acacourierservice.converter;

import com.aca.acacourierservice.entity.PickupPoint;
import com.aca.acacourierservice.entity.Store;
import com.aca.acacourierservice.entity.User;
import com.aca.acacourierservice.model.PickupPointJson;
import com.aca.acacourierservice.model.StoreJson;
import com.aca.acacourierservice.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class StoreConverter implements Converter<Store, StoreJson> {
    private final PickupPointConverter pickupPointConverter;
    private final UserRepository userRepository;

    public StoreConverter(PickupPointConverter pickupPointConverter, UserRepository userRepository) {
        this.pickupPointConverter = pickupPointConverter;
        this.userRepository = userRepository;
    }

    @Override
    public Store convertToEntity(StoreJson model, Store entity) {
        entity.setName(model.getName());
        entity.setPickupPoints(convertToPickupPointList(model.getPickupPoints()));
        entity.setStoreUrl(model.getStoreUrl());
        entity.setApiKey(model.getApiKey());
        entity.setApiSecret(model.getApiSecret());

        Optional<User> adminOptional = userRepository.findById(model.getAdminId());
        if (adminOptional.isPresent()) {
            entity.setAdmin(adminOptional.get());
        }

        return entity;
    }

    @Override
    public Store convertToEntity(StoreJson model) {
        Store entity = new Store();
        entity.setName(model.getName());
        entity.setPickupPoints(convertToPickupPointList(model.getPickupPoints()));
        entity.setStoreUrl(model.getStoreUrl());
        entity.setApiKey(model.getApiKey());
        entity.setApiSecret(model.getApiSecret());

        Optional<User> adminOptional = userRepository.findById(model.getAdminId());
        if (adminOptional.isPresent()) {
            entity.setAdmin(adminOptional.get());
        }

        return entity;
    }

    @Override
    public StoreJson convertToModel(Store entity) {
        StoreJson model = new StoreJson();
        model.setName(entity.getName());
        model.setAdminId(entity.getAdmin().getId());
        model.setStoreUrl(entity.getStoreUrl());
        model.setApiKey(entity.getApiKey());
        model.setApiSecret(model.getApiSecret());
        model.setPickupPoints(convertToPickupPointJsonList(entity.getPickupPoints()));

        return model;
    }

    public List<PickupPoint> convertToPickupPointList(List<PickupPointJson> pickupPointJsons) {
        List<PickupPoint> pickupPoints = new ArrayList<>();
        for (PickupPointJson pickupPointJson : pickupPointJsons) {
            pickupPoints.add(pickupPointConverter.convertToEntity(pickupPointJson));
        }
        return pickupPoints;
    }

    public List<PickupPointJson> convertToPickupPointJsonList(List<PickupPoint> pickupPoints) {
        List<PickupPointJson> pickupPointJsons = new ArrayList<>();
        for (PickupPoint pickupPoint : pickupPoints) {
            pickupPointJsons.add(pickupPointConverter.convertToModel(pickupPoint));
        }
        return pickupPointJsons;
    }
}