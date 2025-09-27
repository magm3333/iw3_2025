package ar.edu.iua.iw3.integration.cli1.model.business;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.edu.iua.iw3.integration.cli1.model.ProductCli1;
import ar.edu.iua.iw3.integration.cli1.model.ProductCli1JsonDeserializer;
import ar.edu.iua.iw3.integration.cli1.model.persistence.ProductCli1Repository;
import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.model.business.FoundException;
import ar.edu.iua.iw3.model.business.ICategoryBusiness;
import ar.edu.iua.iw3.model.business.IProductBusiness;
import ar.edu.iua.iw3.model.business.NotFoundException;
import ar.edu.iua.iw3.model.business.ValidationException;
import ar.edu.iua.iw3.util.JsonUtiles;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductCli1Business implements IProductCli1Business {

	@Autowired(required = false)
	private ProductCli1Repository productDAO;

	@Override
	public ProductCli1 load(String codCli1) throws NotFoundException, BusinessException {
		Optional<ProductCli1> r;
		try {
			r = productDAO.findOneByCodCli1(codCli1);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}
		if (r.isEmpty()) {
			throw NotFoundException.builder().message("No se encuentra el Producto codCli1=" + codCli1).build();
		}
		return r.get();
	}

	@Override
	public List<ProductCli1> list() throws BusinessException {
		try {
			return productDAO.findAll();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}
	}

	@Autowired
	private IProductBusiness productBaseBusiness;

	@Override
	public ProductCli1 add(ProductCli1 product) throws FoundException, BusinessException {

		try {
			productBaseBusiness.load(product.getId());
			throw FoundException.builder().message("Se encontró el Producto id=" + product.getId()).build();
		} catch (NotFoundException e) {
		}

		if (productDAO.findOneByCodCli1(product.getCodCli1()).isPresent()) {
			throw FoundException.builder().message("Se encontró el Producto código=" + product.getCodCli1()).build();
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

	@Override
	public ProductCli1 addExternal(String json) throws FoundException, BusinessException, ValidationException {
		ObjectMapper mapper = JsonUtiles.getObjectMapper(ProductCli1.class,
				new ProductCli1JsonDeserializer(ProductCli1.class, categoryBusiness), null);
		ProductCli1 product = null;
		try {
			product = mapper.readValue(json, ProductCli1.class);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}

		// Validación del nombre
		if (product.getProduct() == null || product.getProduct().isBlank()) {
			throw ValidationException.builder().
			message("El nombre del producto no puede ser nulo ni vacío")
			.build();
		}
		
		return add(product);

	}

}
