package ar.edu.iua.iw3.integration.cli2.model.business;

import ar.edu.iua.iw3.integration.cli2.model.ComponentCli2;
import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.model.business.NotFoundException;

public interface IComponentCli2Business {
    public ComponentCli2 load(String component) throws NotFoundException, BusinessException;
}
