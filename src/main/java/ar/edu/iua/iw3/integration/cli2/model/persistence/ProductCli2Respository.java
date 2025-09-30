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
	
	public List<ProductCli2> findAllOrderByPrecioDesc();
	
	public List<ProductCli2> findByPrecioGreaterThanOrderByPrecioDesc(Double startPrice);
	
	public List<ProductCli2> findByPrecioLessThanOrderByPrecioDesc(Double endPrice);
	
	public List<ProductCli2> findByPrecioBetweenOrderByPrecioDesc(Double startPrice, Double endPrice);
	
	public List<ProductCli2SlimView> findByOrderByPrecioDesc();
}

