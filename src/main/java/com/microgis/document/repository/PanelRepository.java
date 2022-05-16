package com.microgis.document.repository;

import com.microgis.document.Panels;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PanelRepository extends MongoRepository<Panels, Long> {

    Optional<Panels> findById(Long id);

    Optional<Panels> findByNumber(int number);

}