package com.example.catalogservice.service;

import com.example.catalogservice.entity.Catalog;
import com.example.catalogservice.repository.CatalogRepository;
import com.example.catalogservice.vo.ResponseCatalog;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService{

    private final CatalogRepository catalogRepository;
    private final ModelMapper mapper;

    @Override
    public List<ResponseCatalog> getAllCatalogs() {
        return catalogRepository.findAll().stream()
                .map(v -> mapper.map(v, ResponseCatalog.class))
                .collect(Collectors.toList());
    }
}
