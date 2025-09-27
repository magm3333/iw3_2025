package ar.edu.iua.iw3.integration.cli1.model.business;

import java.util.List;

import ar.edu.iua.iw3.integration.cli1.model.ProductCli1;
import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.model.business.FoundException;
import ar.edu.iua.iw3.model.business.NotFoundException;
import ar.edu.iua.iw3.model.business.ValidationException;

public interface IProductCli1Business {
	public ProductCli1 load(String codCli1) throws NotFoundException, BusinessException;
	public List<ProductCli1> list() throws BusinessException;
	public ProductCli1 add(ProductCli1 product) throws FoundException, BusinessException;
	
	public ProductCli1 addExternal(String json) throws FoundException, BusinessException, ValidationException;

}
