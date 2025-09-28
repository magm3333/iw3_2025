package ar.edu.iua.iw3.integration.cli2.model;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashSet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import ar.edu.iua.iw3.integration.cli2.model.business.IComponentCli2Business;
import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.model.business.ICategoryBusiness;
import ar.edu.iua.iw3.model.business.NotFoundException;
import ar.edu.iua.iw3.util.JsonUtiles;

public class ProductCli2JsonDeserializer extends StdDeserializer<ProductCli2> {
    private ICategoryBusiness categoryBusiness;
    private IComponentCli2Business componentBusiness;

    protected ProductCli2JsonDeserializer(Class<?> vc) {
        super(vc);
    }

    public ProductCli2JsonDeserializer(Class<?> vc, ICategoryBusiness categoryBusiness,
            IComponentCli2Business componentBusiness) {
        super(vc);
        this.categoryBusiness = categoryBusiness;
        this.componentBusiness = componentBusiness;
    }

    @Override
    public ProductCli2 deserialize(com.fasterxml.jackson.core.JsonParser jp,
            com.fasterxml.jackson.databind.DeserializationContext ctxt)
            throws java.io.IOException, com.fasterxml.jackson.core.JacksonException {
        ProductCli2 r = new ProductCli2();
        com.fasterxml.jackson.databind.JsonNode node = jp.getCodec().readTree(jp);

        String productDesc = JsonUtiles.getString(node,
                "product,description,product_description,product_name".split(","), null);
        double price = JsonUtiles.getDouble(node, "product_price,price_product,price".split(","), 0);
        boolean stock = JsonUtiles.getBoolean(node, "stock,in_stock".split(","), false);
        r.setProduct(productDesc);
        // Si el campo 'product description' es nulo o vacío, lanzamos una excepción. Se
        // tomo esta decisión para asegurar que
        // siempre se proporcione una descripción del producto ya que se considera un
        // campo obligatorio por la relevancia que tiene ya que no deberia poder
        // existir un producto sin un nombre o descripcion.
        if (productDesc == null || productDesc.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo 'product description' es obligatorio y no puede estar vacío.");
        }
        r.setPrecio(price);
        r.setStock(stock);
        String categoryName = JsonUtiles.getString(node, "category,product_category,category_product".split(","), null);
        if (categoryName != null) {
            try {
                r.setCategory(categoryBusiness.load(categoryName));
            } catch (Exception e) {
            }
        }
        Date expirationDate;
        try {
            expirationDate = JsonUtiles.getDate(node, "expirationDate,expiration_date".split(","), null);
            if (expirationDate == null) {
                throw new IllegalArgumentException("El campo 'expirationDate' es obligatorio y no puede estar vacío.");
            }

            r.setExpirationDate(expirationDate);
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                    "El campo 'expirationDate' tiene un formato inválido. Se esperaba ISO-8601.");
        }

        String[] componentsStr = JsonUtiles.getArrayComponent(node,
                "components,parts".split(","), null);

        if (componentsStr != null && componentsStr.length > 0) {
            if (r.getComponents() == null) {
                r.setComponents(new HashSet<>());
            }

            for (String comp : componentsStr) {
                comp = comp.trim();
                try {
                    r.getComponents().add(componentBusiness.load(comp));
                } catch (NotFoundException e) {
                    throw new IllegalArgumentException(
                            "El componente '" + comp + "' no existe. Solo se pueden asociar componentes existentes.");
                } catch (BusinessException e) {
                    throw new IllegalArgumentException(
                            "Error al cargar el componente '" + comp + "'.", e);
                }
            }
        }

        return r;
    }

}
