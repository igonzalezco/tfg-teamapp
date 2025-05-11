package com.unir.teamapp.persist.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.entity.Usuario;
import com.unir.teamapp.persist.repository.common.ComplexJpaRepository;

@Repository("usuarioRepository")
public interface UsuarioRepository extends ComplexJpaRepository<Usuario, Integer> {

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.rol r WHERE u.email = :username")
    Optional<Usuario> findByLoginWithRol(@Param("username") String username);

}
