package ar.edu.iua.iw3.integration.cli2.model.business;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ar.edu.iua.iw3.integration.cli2.model.ProductCli2;
import ar.edu.iua.iw3.integration.cli2.model.ProductCli2JsonDeserializer;
import ar.edu.iua.iw3.integration.cli2.model.ProductCli2SlimView;
import ar.edu.iua.iw3.integration.cli2.model.persistence.ProductCli2Respository;
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
public class ProductCli2Business implements IProductCli2Business {

	@Autowired(required = false)
	private ProductCli2Respository productDAO;

	@Autowired
	private IProductBusiness productBaseBusiness;

	@Override
	public ProductCli2 add(ProductCli2 product) throws FoundException, BusinessException {
		try {
			productBaseBusiness.load(product.getId());
			throw FoundException.builder().message("Se encontró el Producto id=" + product.getId()).build();
		} catch (NotFoundException e) {
		}

		try {
			return productDAO.save(product);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}
	}

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

	@Autowired(required = false)
	private ICategoryBusiness categoryBusiness;

	@Override
	public ProductCli2 addExternal(String json) throws FoundException, BusinessException, ValidationException {
    // Creamos el ObjectMapper con nuestro deserializer de CLI2
    ObjectMapper mapper = JsonUtiles.getObjectMapper(ProductCli2.class,
            new ProductCli2JsonDeserializer(ProductCli2.class, categoryBusiness), null);

    ProductCli2 product = null;
    try {
        product = mapper.readValue(json, ProductCli2.class);
    } catch (JsonProcessingException e) {
        log.error(e.getMessage(), e);
        throw BusinessException.builder().ex(e).build();
    }

    //valido nombnre
    if (product.getProduct() == null || product.getProduct().isBlank()) {
        throw ValidationException.builder()
                .message("El nombre del producto no puede ser nulo ni vacío")
                .build();
    }

    //valido vto
    if (product.getExpirationDate() == null) {
        throw ValidationException.builder()
                .message("La fecha de vencimiento no puede ser nula")
                .build();
    }

    // valido componentes
    /*if (product.getComponents() == null || product.getComponents().isEmpty()) {
        throw ValidationException.builder()
                .message("El producto debe tener al menos un componente asociado")
                .build();
    }*/
    return add(product);
}


	@Override
	public List<ProductCli2> listAllOrderByPrice() throws BusinessException {
		try {
			return productDAO.findByOrderByPrecioAsc();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}
	}

	@Override
	public List<ProductCli2> listByPriceRange(Double startPrice, Double endPrice) throws BusinessException {
		try {
			if (startPrice == null) {
				return productDAO.findByPrecioLessThanEqualOrderByPrecioAsc(endPrice);
			} else if (endPrice == null) {
				return productDAO.findByPrecioGreaterThanEqualOrderByPrecioAsc(startPrice);
			} else {
				return productDAO.findByPrecioBetweenOrderByPrecioAsc(startPrice, endPrice);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}
	}

}

