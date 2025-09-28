package ar.edu.iua.iw3.integration.cli2.model.persistence;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.iua.iw3.integration.cli2.model.ProductCli2;
import ar.edu.iua.iw3.integration.cli2.model.ProductCli2SlimView;

@Repository
public interface ProductCli2Respository extends JpaRepository<ProductCli2, Long> {
	Optional<ProductCli2> findOneById(long id);
	
	public List<ProductCli2> findByExpirationDateBeforeOrderByExpirationDateDesc(Date expirationDate);
	
	public List<ProductCli2SlimView> findByOrderByPrecioDesc();

	public List<ProductCli2> findByOrderByPrecioAsc();

	public List<ProductCli2> findByPrecioLessThanEqualOrderByPrecioAsc(Double precio);

	public List<ProductCli2> findByPrecioGreaterThanEqualOrderByPrecioAsc(Double precio);

	public List<ProductCli2> findByPrecioBetweenOrderByPrecioAsc(Double startPrice, Double endPrice);
}

