package ar.edu.iua.iw3.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.iua.iw3.model.business.BusinessException;
import ar.edu.iua.iw3.util.EmailBusiness;
import ar.edu.iua.iw3.util.IStandartResponseBusiness;

@RestController
@RequestMapping(Constants.URL_MAIL)
public class MailRestController extends BaseRestController {
	@Autowired
	private IStandartResponseBusiness response;
	@Autowired
	private EmailBusiness emailBusiness;
	@Value("${spring.profiles.active}")
	private String profile;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/test")
	public ResponseEntity<?> test(@RequestParam String to, @RequestParam String subject) {
		try {
			emailBusiness.sendSimpleMessage(to, subject,
					String.format("Prueba perfil activo=%s Fecha/Hora=%s", profile, new Date()));
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
