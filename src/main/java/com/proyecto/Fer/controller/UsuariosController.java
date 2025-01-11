package com.proyecto.Fer.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.Fer.model.UsuariosModel;
import com.proyecto.Fer.model.DTO.UsuariosDTO;
import com.proyecto.Fer.repository.UsuariosRepo;
import com.proyecto.Fer.service.UsuariosService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@CrossOrigin("*")
public class UsuariosController {

	@Value("${jwt.secret.key}")
	String secretKey;
	@Value("${jwt.time.expiration}")
	String timeExpiration;

	@Autowired
	public UsuariosRepo datosrep;

	// Listar Usuarios
	@GetMapping("/usuario_model")
	public List<UsuariosModel> listaUsuarios() {
		return datosrep.findAll();
	}

	@Autowired
	public UsuariosService service;

	
	
	@GetMapping("/UsuariosDTO")
	public List<UsuariosDTO> listaUsuariosDTO() {
		return service.findByUsuarios();
	}
	
	
	//TOKEN
	@PostMapping("/api/login")
	public UsuariosModel acceso(@RequestParam("login") String xlogin, @RequestParam("passwd") String xpass ) {
		UsuariosModel user= new UsuariosModel();
		user = datosrep.verificarCuentaUsuario(xlogin, xpass);		
		if (user != null) {
			try {
				String xtoken = getJWTToken(xlogin);
				System.out.println("este es mi TOKEN generado::"+ xtoken);
				user.setToken(xtoken);
			} catch (Exception e) {
				user = null;
			}
		}else {
			System.out.println("ACCESO NO PERMITIDO...");
		}
		return user;
	}
	
	

	private String getJWTToken(String username) {
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setId("softtekJWT")
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() +Long.parseLong(timeExpiration)))
				//.setExpiration(new Date(System.currentTimeMillis() + 600000 ))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();
		return "Bearer " + token;
	}

	// Eliminar Usuarios
	@DeleteMapping("/Usuarios/{xlogin}")
	public void deleteUsers(@PathVariable String xlogin) {
		datosrep.eliminarUsuario(xlogin);

	}

	// Insertar Usuarios
	@PostMapping("/Usuarios")
	public void insertUsers(@RequestBody UsuariosModel xUsers) {
		datosrep.insertUsers(xUsers.getLogin(), xUsers.getPasswd(), xUsers.getEstado());
	}

	// Modificar Usuarios
	@PutMapping("/Usuarios/{xlogin}")
	public void modificarUsuarios(@RequestBody UsuariosModel xUsers, @PathVariable String xlogin) {
		datosrep.modifyUsers(xlogin, xUsers.getPasswd(), xUsers.getEstado());
	}

}
