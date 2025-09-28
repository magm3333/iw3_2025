package ar.edu.iua.iw3.integration.cli2.model;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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

    private ICategoryBusiness categoryBusiness;

    public ProductCli2JsonDeserializer(Class<?> vc, ICategoryBusiness categoryBusiness) {
        super(vc);
        this.categoryBusiness = categoryBusiness;
    }

    @Override
    public ProductCli2 deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JacksonException {

        ProductCli2 r = new ProductCli2();
        JsonNode node = jp.getCodec().readTree(jp);

        // Campos básicos
        r.setProduct(JsonUtiles.getString(node, "product,description,product_name".split(","), null));
        r.setPrecio(JsonUtiles.getDouble(node, "precio,price_product,product_price".split(","), 0));
        r.setStock(JsonUtiles.getBoolean(node, "stock,in_stock".split(","), false));
        r.setExpirationDate(JsonUtiles.getDate(node,"expirationDate,expiration_date".split(","), new java.sql.Date(System.currentTimeMillis())));

        // Categoría
        String categoryName = JsonUtiles.getString(node, "category,product_category,category_product".split(","), null);
        if (categoryName != null) {
            try {
                r.setCategory(categoryBusiness.load(categoryName));
            } catch (NotFoundException | BusinessException e) {
            }
        }

        if (node.has("components")) {
            Set<ComponentCli2> components = new HashSet<>();
            for (JsonNode compNode : node.get("components")) {
                ComponentCli2 c = new ComponentCli2();
                c.setId(JsonUtiles.getLong(compNode, "id,comp_id,component_id".split(","),Long.valueOf(0)));
                c.setComponent(JsonUtiles.getString(compNode, "component,name".split(","), null));
                components.add(c);
            }
            r.setComponents(components);
        }

        return r;
    }
}