package com.proyecto.Fer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.Fer.config.ApiResponse;
import com.proyecto.Fer.model.*;
import com.proyecto.Fer.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/asignaciones")
@CrossOrigin(origins = "*")
public class AsignacionController {

    @Autowired
    private UsuariosRepo usuariosRepo;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UserRoleMappingRepository userRoleMappingRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RoleMenuMappingRepository roleMenuMappingRepository;

       // Listar usuarios
       @GetMapping("/usuarios")
       public ResponseEntity<List<UsuariosModel>> listarUsuarios() {
           return ResponseEntity.ok(usuariosRepo.findAll());
       }
    @PostMapping("/usuario-rol")
    public ResponseEntity<?> asignarRolAUsuario(@RequestParam String login, @RequestParam Long rolId) {
        Optional<UsuariosModel> usuarioOpt = usuariosRepo.findByLogin(login);
        Optional<RolModel> rolOpt = rolRepository.findById(rolId);

        if (usuarioOpt.isPresent() && rolOpt.isPresent()) {
            UserRoleMapping userRole = new UserRoleMapping();
            userRole.setUsuario(usuarioOpt.get());
            userRole.setRol(rolOpt.get());
            userRoleMappingRepository.save(userRole);
            return ResponseEntity.ok("Rol asignado correctamente.");
        }
        return ResponseEntity.badRequest().body("Usuario o rol no encontrado.");
    }

     // Listar roles de un usuario
    @GetMapping("/usuario/{login}/roles")
    public ResponseEntity<List<RolModel>> listarRolesDeUsuario(@PathVariable String login) {
        List<UserRoleMapping> userRoles = userRoleMappingRepository.findByUsuarioLogin(login);
        List<RolModel> roles = userRoles.stream()
            .map(UserRoleMapping::getRol)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(roles);
    }

     // Desasignar rol a un usuario
     @DeleteMapping("/usuario-rol")
     public ResponseEntity<?> desasignarRolDeUsuario(@RequestParam String login, @RequestParam Long rolId) {
         List<UserRoleMapping> userRoles = userRoleMappingRepository.findByUsuarioLogin(login);
         
         Optional<UserRoleMapping> roleToRemove = userRoles.stream()
             .filter(ur -> ur.getRol().getIdRol().equals(rolId))
             .findFirst();
         
         if (roleToRemove.isPresent()) {
             userRoleMappingRepository.delete(roleToRemove.get());
             return ResponseEntity.ok("Rol desasignado correctamente.");
         }
         
         return ResponseEntity.badRequest().body("Rol no encontrado para este usuario.");
     }

         // Desasignar menú de un rol
         @DeleteMapping("/rol-menu")
         public ResponseEntity<?> desasignarMenuDeRol(@RequestParam Long rolId, @RequestParam Long menuId) {
             List<RoleMenuMapping> roleMenus = roleMenuMappingRepository.findByRolIdRol(rolId);
             
             Optional<RoleMenuMapping> menuToRemove = roleMenus.stream()
                 .filter(rm -> rm.getMenu().getIdMenu().equals(menuId))
                 .findFirst();
             
             if (menuToRemove.isPresent()) {
                 roleMenuMappingRepository.delete(menuToRemove.get());
                 return ResponseEntity.ok(new ApiResponse("Menú desasignado correctamente del rol."));
             }
             
             return ResponseEntity.badRequest().body(new ApiResponse("Menú no encontrado para este rol."));
         }
         

@PostMapping("/rol-menu")
public ResponseEntity<?> asignarMenuARol(@RequestParam Long rolId, @RequestParam Long menuId) {
    Optional<RolModel> rolOpt = rolRepository.findById(rolId);
    Optional<MenuModel> menuOpt = menuRepository.findById(menuId);

    if (rolOpt.isPresent() && menuOpt.isPresent()) {
        RoleMenuMapping roleMenu = new RoleMenuMapping();
        roleMenu.setRol(rolOpt.get());
        roleMenu.setMenu(menuOpt.get());
        roleMenuMappingRepository.save(roleMenu);
        return ResponseEntity.ok(new ApiResponse("Menú asignado correctamente al rol."));
    }
    return ResponseEntity.badRequest().body(new ApiResponse("Rol o menú no encontrado."));
}




}
