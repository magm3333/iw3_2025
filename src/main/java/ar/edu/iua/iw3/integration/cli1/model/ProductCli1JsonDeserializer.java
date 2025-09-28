package ar.edu.iua.iw3.integration.cli1.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.model.business.ICategoryBusiness;
import ar.edu.iua.iw3.model.business.NotFoundException;
import ar.edu.iua.iw3.util.JsonUtiles;

public class ProductCli1JsonDeserializer extends StdDeserializer<ProductCli1> {

	protected ProductCli1JsonDeserializer(Class<?> vc) {
		super(vc);
	}

	private ICategoryBusiness categoryBusiness;

	public ProductCli1JsonDeserializer(Class<?> vc, ICategoryBusiness categoryBusiness) {
		super(vc);
		this.categoryBusiness = categoryBusiness;
	}

	@Override
	public ProductCli1 deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
		ProductCli1 r = new ProductCli1();
		JsonNode node = jp.getCodec().readTree(jp);

		String code = JsonUtiles.getString(node, "product_code,code_product,code".split(","),
				System.currentTimeMillis() + "");
		String productDesc = JsonUtiles.getString(node,
				"product,description,product_description,product_name".split(","), null);
		// Si el campo 'product description' es nulo o vacío, lanzamos una excepción. Se tomo esta decisión para asegurar que
		// siempre se proporcione una descripción del producto ya que se considera un campo obligatorio por la relevancia que tiene ya que no deberia poder
		// existir un producto sin un nombre o descripcion.
		if (productDesc == null || productDesc.trim().isEmpty()) {
			throw new IllegalArgumentException("El campo 'product description' es obligatorio y no puede estar vacío.");
		}
		double price = JsonUtiles.getDouble(node, "product_price,price_product,price".split(","), 0);
		boolean stock = JsonUtiles.getBoolean(node, "stock,in_stock".split(","), false);
		r.setCodCli1(code);
		r.setProduct(productDesc);
		r.setPrecio(price);
		r.setStock(stock);
		String categoryName = JsonUtiles.getString(node, "category,product_category,category_product".split(","), null);
		if (categoryName != null) {
			try {
				r.setCategory(categoryBusiness.load(categoryName));
			} catch (NotFoundException | BusinessException e) {
			}
		}
		return r;
	}

}
