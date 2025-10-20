package ar.edu.iua.iw3.config.profile;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.iua.iw3.auth.IUserBusiness;
import ar.edu.iua.iw3.auth.User;
import ar.edu.iua.iw3.controllers.BaseRestController;
import ar.edu.iua.iw3.controllers.Constants;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(Constants.URL_AUTHORIZATION)
public class AuthtestRestController extends BaseRestController {

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/admin")
	public ResponseEntity<String> onlyAdmin() {
		return new ResponseEntity<String>("Servicio admin", HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/user")
	public ResponseEntity<String> onlyUser() {
		return new ResponseEntity<String>("Servicio user", HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/user-or-admin")
	public ResponseEntity<String> rolUserOArdmin() {
		return new ResponseEntity<String>("Servicio user or admin", HttpStatus.OK);
	}

//Se compara con los datos de entrada
	@PreAuthorize("#username == authentication.principal.username")
	@GetMapping("/my-rols")
	public ResponseEntity<String> myRols(@RequestParam String username) {
		return new ResponseEntity<String>(getUserLogged().getAuthorities().toString(), HttpStatus.OK);
	}

	@GetMapping("/variable")
	public ResponseEntity<String> variable(HttpServletRequest request) {
		if (request.isUserInRole("ROLE_ADMIN")) {
			return new ResponseEntity<String>("Tenés rol admin", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("No tenés rol admin", HttpStatus.OK);
		}
	}

	//Se compara con los datos de respuesta
	@PostAuthorize("returnObject.username == #username")
	@GetMapping("/full-data")
	public User fullData(@RequestParam String username) {
		return getUserLogged();
	}

	@Autowired
	private IUserBusiness userBusiness;

	//El user actual no figura en la lista
	@PostFilter("filterObject != authentication.principal.username")
	@GetMapping("/self-filter")
	public List<String> selfFilter() {
		List<String> r = null;
		try {
			r = userBusiness.list().stream().map(u -> u.getUsername()).collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return r;
	}

}