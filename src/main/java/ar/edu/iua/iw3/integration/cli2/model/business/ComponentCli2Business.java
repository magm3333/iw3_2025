package ar.edu.iua.iw3.integration.cli2.model.business;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.iua.iw3.integration.cli2.model.ComponentCli2;
import ar.edu.iua.iw3.integration.cli2.model.persistence.ComponentCli2Repository;
import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.model.business.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ComponentCli2Business implements IComponentCli2Business {
	@Autowired(required = false)
    private ComponentCli2Repository componentCli2DAO;
    @Override
	public ComponentCli2 load(String component) throws NotFoundException, BusinessException {
		Optional<ComponentCli2> r;
		try {
			r = componentCli2DAO.findByComponent(component);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw BusinessException.builder().ex(e).build();
		}
		if (r.isEmpty()) {
			throw NotFoundException.builder().message("No se encuentra el componente=" + component).build();
		}
		return r.get();
	}
}
