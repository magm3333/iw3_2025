package ar.edu.iua.iw3.config.profile;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
// Sin filtros de exclusión para permitir ambos clientes en producción
@EnableJpaRepositories(basePackages = "ar.edu.iua.iw3")

// Entidades de ambos clientes + core
@EntityScan(basePackages = { 
    "ar.edu.iua.iw3.model", 
    "ar.edu.iua.iw3.auth", 
    "ar.edu.iua.iw3.integration.cli1.model",
    "ar.edu.iua.iw3.integration.cli2.model" 
})

@Profile("mysqlprod-multicliente")
public class MysqlprodMulticlienteScanConfig {

}