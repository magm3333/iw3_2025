package ar.edu.iua.iw3.integration.cli2.model;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.model.business.ICategoryBusiness;
import ar.edu.iua.iw3.model.business.NotFoundException;
import ar.edu.iua.iw3.util.JsonUtiles;

public class ProductCli2JsonDeserializer extends StdDeserializer<ProductCli2> {


	protected ProductCli2JsonDeserializer(Class<?> vc) {
		super(vc);
	}

	private ICategoryBusiness categoryBusiness;

	public ProductCli2JsonDeserializer(Class<?> vc, ICategoryBusiness categoryBusiness) {
		super(vc);
		this.categoryBusiness = categoryBusiness;
	}

	@Override
	public ProductCli2 deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
		ProductCli2 r = new ProductCli2();
		JsonNode node = jp.getCodec().readTree(jp);

		String fecha = JsonUtiles.getString(node, "date, datetime, prod_date".split(","),null);
		String code = JsonUtiles.getString(node, "product_code,code_product,code".split(","),
				System.currentTimeMillis() + "");
		String productDesc = JsonUtiles.getString(node,
				"product,description,product_description,product_name".split(","), null);
		double price = JsonUtiles.getDouble(node, "product_price,price_product,price".split(","), 0);
		boolean stock = JsonUtiles.getBoolean(node, "stock,in_stock".split(","), false);
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//Date dia = "2025-09-28T21:51:24.379-03:00";
		if (fecha != null && !fecha.isBlank()) {
		    try {
		        //SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		        Date dia = formato.parse(fecha);
		        r.setExpirationDate(dia);
		    } catch (ParseException e) {
		        // loggear si querés debug
		    }
		}		
		
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
