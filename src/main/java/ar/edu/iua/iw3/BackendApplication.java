package ar.edu.iua.iw3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class BackendApplication extends SpringBootServletInitializer implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Value("${spring.profiles.active}")
	private String profile;

	/*@Autowired
	private IProductCli2Business productCli2Business;

	@Autowired
	private ProductRepository productDAO;*/


	@Override
	public void run(String... args) throws Exception {
		log.info("Perfil Activo '{}'", profile);
		/*log.info(
				"Default -------------------------------------------------------------------------------------------------------");
		productCli2Business.listExpired(new Date());
		log.info(
				"Customizada ---------------------------------------------------------------------------------------------------");
		productCli2Business.listSlim();
		
		log.info("Cantidad de productos de la categoría id=3: {}", productDAO.countProductsByCategory(3));
		log.info("Set stock=true producto id que no existe, resultado={}", productDAO.setStock(true, 333));*/


	}

}
