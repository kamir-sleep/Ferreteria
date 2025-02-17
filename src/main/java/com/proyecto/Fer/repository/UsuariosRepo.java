package com.proyecto.Fer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.Fer.DTO.UsuariosDTO;
import com.proyecto.Fer.model.UsuariosModel;

import jakarta.transaction.Transactional;

public interface UsuariosRepo extends JpaRepository<UsuariosModel, String> {

    
    //DTO
	List<UsuariosDTO> findBy();
	 // Buscar usuario por login
     Optional<UsuariosModel> findByLogin(String login);
     boolean existsByLogin(String login);

	//Eliminar Usuarios
	@Modifying
	@Transactional
	@Query(nativeQuery = false, value = "delete from UsuariosModel u where u.login=:xlogin")
	void eliminarUsuario(@Param("xlogin") String xlogin);

	// Insertar Usuarios
    @Modifying
    @Transactional
    @Query("INSERT INTO UsuariosModel (login, passwd, estado) "
    		+ "VALUES (:xlogin, :xpasswd, :xestado)")
    void insertUsers(@Param("xlogin") String xlogin, 
                     @Param("xpasswd") String xpasswd, 
                     @Param("xestado") int xestado);
	
	// Modificar Usuarios
    @Modifying
    @Transactional
    @Query("UPDATE UsuariosModel u SET u.passwd = :xpasswd, u.estado = :xestado "
    		+ "WHERE u.login = :xlogin")
    void modifyUsers(@Param("xlogin") String xlogin, 
                     @Param("xpasswd") String xpasswd, 
                     @Param("xestado") int xestado);

	

    //TOKEN
    @Query("select u " 
    + " from UsuariosModel u " 
    + " where 	u.login=?1 and " 
    + "	u.passwd=?2 ")
    UsuariosModel verificarCuentaUsuario(String xlogin, String xpasswd);


    //CAMBIAR CONTRASEÑA
    @Modifying
    @Transactional
    @Query("UPDATE UsuariosModel u SET u.passwd = :nuevaPassword WHERE u.login = :login")
    void cambiarPassword(@Param("login") String login, @Param("nuevaPassword") String nuevaPassword);

}
