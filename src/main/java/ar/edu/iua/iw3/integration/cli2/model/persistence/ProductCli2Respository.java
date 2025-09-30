package ar.edu.iua.iw3.integration.cli2.model.persistence;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.iua.iw3.integration.cli2.model.ProductCli2;
import ar.edu.iua.iw3.integration.cli2.model.ProductCli2SlimView;
import ar.edu.iua.iw3.model.business.BusinessException;

@Repository
public interface ProductCli2Respository extends JpaRepository<ProductCli2, Long> {
	
	public List<ProductCli2> findByExpirationDateBeforeOrderByExpirationDateDesc(Date expirationDate);
	
	public List<ProductCli2> findAllByOrderByPrecioAsc();
	
	public List<ProductCli2> findByPrecioGreaterThanOrderByPrecio(Double startPrice);
	
	public List<ProductCli2> findByPrecioLessThanOrderByPrecio(Double endPrice);
	
	public List<ProductCli2> findByPrecioBetweenOrderByPrecio(Double startPrice, Double endPrice);
	
	public List<ProductCli2SlimView> findByOrderByPrecioDesc();
}

