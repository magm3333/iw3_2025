package ar.edu.iua.iw3.integration.cli2.model.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.iua.iw3.integration.cli2.model.ComponentCli2;

public interface ComponentCli2Repository extends JpaRepository<ComponentCli2, Long> {
    Optional<ComponentCli2> findByComponent(String component);
}
