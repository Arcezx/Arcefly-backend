package com.example.prueba.repositories;

import com.example.prueba.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByTipoUsuario(String tipoUsuario);

    List<Usuario> findByTipoUsuarioIn(List<String> tipos);

    List<Usuario> findByNombreContainingIgnoreCaseOrEmailContainingIgnoreCase(String nombre, String email);

    boolean existsByEmail(String email);

    List<Usuario> findByTipoUsuarioInAndEstado(List<String> tipos, String estado);

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.tipoUsuario = :tipoUsuario")
    long countByTipoUsuario(String tipoUsuario);


    @Query("SELECT u FROM Usuario u WHERE u.tipoUsuario = 'ADMIN'")
    List<Usuario> findAllAdminsWithDetails();

    long countByTipoUsuarioAndEstado(String tipoUsuario, String estado);
    Optional<Usuario> findByEmailAndPassword(String email, String password);

}