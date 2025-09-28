package ar.edu.iua.iw3.integration.cli2.model.business;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.edu.iua.iw3.integration.cli1.model.ProductCli1;
import ar.edu.iua.iw3.integration.cli1.model.ProductCli1JsonDeserializer;
import ar.edu.iua.iw3.integration.cli2.model.ProductCli2;
import ar.edu.iua.iw3.integration.cli2.model.ProductCli2JsonDeserializer;
import ar.edu.iua.iw3.integration.cli2.model.ProductCli2SlimView;
import ar.edu.iua.iw3.integration.cli2.model.persistence.ProductCli2Respository;
import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.model.business.FoundException;
import ar.edu.iua.iw3.model.business.ICategoryBusiness;
import ar.edu.iua.iw3.model.business.IProductBusiness;
import ar.edu.iua.iw3.model.business.NotFoundException;
import ar.edu.iua.iw3.util.JsonUtiles;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductCli2Business implements IProductCli2Business {

	@Autowired(required = false)
	private ProductCli2Respository productDAO;

	@Override
	public List<ProductCli2> listExpired(Date date) throws BusinessException {
		try {
			return productDAO.findByExpirationDateBeforeOrderByExpirationDateDesc(date);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}
	}

	@Override
	public List<ProductCli2SlimView> listSlim() throws BusinessException {
		try {
			return productDAO.findByOrderByPrecioDesc();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}
	}

	@Autowired
	private IProductBusiness productBaseBusiness;

	@Override
	public ProductCli2 add(ProductCli2 product) throws FoundException, BusinessException {

		try {
			productBaseBusiness.load(product.getId());
			throw FoundException.builder().message("Se encontró el Producto id=" + product.getId()).build();
		} catch (NotFoundException e) {
		}

		if (productDAO.findOneByProduct(product.getProduct()) != null) {
			throw FoundException.builder().message("Se encontró el Producto " + product.getProduct()).build();
		}


		try {
			return productDAO.save(product);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}
	}

	@Autowired(required = false)
	private ICategoryBusiness categoryBusiness;
	@Autowired(required = false)
	private IComponentCli2Business componentBusiness;

	@Override
	public ProductCli2 addExternal(String json) throws FoundException, BusinessException {
		ObjectMapper mapper = JsonUtiles.getObjectMapper(ProductCli2.class,
				new ProductCli2JsonDeserializer(ProductCli2.class, categoryBusiness, componentBusiness), null);
				
		ProductCli2 product = null;
		try {
			product = mapper.readValue(json, ProductCli2.class);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}

		return add(product);

	}

}
