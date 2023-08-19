package com.aca.acacourierservice.converter;

public interface Converter <E,M>{
    E convertToEntity(M model,E entity);
    E convertToEntity(M model);
    M convertToModel(E entity,M model);
}
