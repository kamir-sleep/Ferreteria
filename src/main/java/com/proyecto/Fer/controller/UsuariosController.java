package com.proyecto.Fer.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.Fer.DTO.RolDTO;
import com.proyecto.Fer.DTO.UsuariosDTO;
import com.proyecto.Fer.model.MenuModel;
import com.proyecto.Fer.model.RoleMenuMapping;
import com.proyecto.Fer.model.UserRoleMapping;
import com.proyecto.Fer.model.UsuariosModel;
import com.proyecto.Fer.repository.RoleMenuMappingRepository;
import com.proyecto.Fer.repository.UserRoleMappingRepository;
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

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleMenuMappingRepository roleMenuMappingRepository;

	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;

	
	
	@GetMapping("/UsuariosDTO")
	public List<UsuariosDTO> listaUsuariosDTO() {
		return service.findByUsuarios();
	}
	
	
@PostMapping("/api/login")
public ResponseEntity<?> acceso(@RequestParam("login") String xlogin, @RequestParam("passwd") String xpass) {
    Optional<UsuariosModel> optionalUser = datosrep.findByLogin(xlogin);
    
    if (optionalUser.isPresent()) {
        UsuariosModel user = optionalUser.get();

        if (passwordEncoder.matches(xpass, user.getPasswd())) {
            String xtoken = getJWTToken(xlogin);
            
			System.out.println(xtoken);
            // Fetch user roles
            List<UserRoleMapping> userRoles = userRoleMappingRepository.findByUsuarioLogin(xlogin);
            List<RolDTO> roles = userRoles.stream()
                .map(mapping -> new RolDTO(mapping.getRol().getIdRol(), mapping.getRol().getNombre()))
                .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("login", user.getLogin());
            response.put("estado", user.getEstado());
            response.put("token", xtoken);
            response.put("roles", roles);
			 System.out.println(roles);

            return ResponseEntity.ok(response);
        }
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Authentication failed"));
}

// New endpoint to get menus for a specific role
@GetMapping("/api/menus/role/{rolId}")
public ResponseEntity<List<MenuModel>> getMenusByRole(@PathVariable Long rolId) {
    List<RoleMenuMapping> roleMenus = roleMenuMappingRepository.findByRolIdRol(rolId);
    List<MenuModel> menus = roleMenus.stream()
        .map(RoleMenuMapping::getMenu)
        .collect(Collectors.toList());
    
    return ResponseEntity.ok(menus);
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


 // Listar usuarios con sus roles asignados
    @GetMapping("/api/usuarios/with-roles")
    public ResponseEntity<List<Map<String, Object>>> listarUsuariosConRoles() {
        List<UsuariosModel> usuarios = datosrep.findAll();
        List<Map<String, Object>> usuariosConRoles = new ArrayList<>();

        for (UsuariosModel usuario : usuarios) {
            Map<String, Object> usuarioInfo = new HashMap<>();
            usuarioInfo.put("login", usuario.getLogin());
            usuarioInfo.put("estado", usuario.getEstado());

            // Obtener roles del usuario
            List<UserRoleMapping> userRoles = userRoleMappingRepository.findByUsuarioLogin(usuario.getLogin());
            List<Map<String, Object>> roles = userRoles.stream()
                .map(mapping -> {
                    Map<String, Object> rol = new HashMap<>();
                    rol.put("idRol", mapping.getRol().getIdRol());
                    rol.put("nombre", mapping.getRol().getNombre());
                    return rol;
                })
                .collect(Collectors.toList());

            usuarioInfo.put("roles", roles);
            usuariosConRoles.add(usuarioInfo);
        }

        return ResponseEntity.ok(usuariosConRoles);
    }

    // Obtener estadísticas de usuarios
    @GetMapping("/api/usuarios/stats")
    public ResponseEntity<Map<String, Long>> obtenerEstadisticasUsuarios() {
        List<UsuariosModel> usuarios = datosrep.findAll();
        
        long totalUsuarios = usuarios.size();
        long usuariosActivos = usuarios.stream()
            .filter(u -> u.getEstado() == 1)
            .count();

        Map<String, Long> estadisticas = new HashMap<>();
        estadisticas.put("totalUsuarios", totalUsuarios);
        estadisticas.put("usuariosActivos", usuariosActivos);

        return ResponseEntity.ok(estadisticas);
    }

    // Activar usuario
    @PutMapping("/api/usuarios/{login}/activar")
    public ResponseEntity<?> activarUsuario(@PathVariable String login) {
        Optional<UsuariosModel> usuarioOpt = datosrep.findByLogin(login);
        
        if (usuarioOpt.isPresent()) {
            UsuariosModel usuario = usuarioOpt.get();
            usuario.setEstado(1); // 1 = activo
            datosrep.save(usuario);
            return ResponseEntity.ok(Map.of("mensaje", "Usuario activado correctamente"));
        }
        
        return ResponseEntity.notFound().build();
    }

    // Desactivar usuario
    @PutMapping("/api/usuarios/{login}/desactivar")
    public ResponseEntity<?> desactivarUsuario(@PathVariable String login) {
        Optional<UsuariosModel> usuarioOpt = datosrep.findByLogin(login);
        
        if (usuarioOpt.isPresent()) {
            UsuariosModel usuario = usuarioOpt.get();
            usuario.setEstado(0); // 0 = inactivo
            datosrep.save(usuario);
            return ResponseEntity.ok(Map.of("mensaje", "Usuario desactivado correctamente"));
        }
        
        return ResponseEntity.notFound().build();
    }




}
