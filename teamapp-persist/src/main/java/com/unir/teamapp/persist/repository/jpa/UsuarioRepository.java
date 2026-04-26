package com.unir.teamapp.persist.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.entity.Usuario;
import com.unir.teamapp.persist.entity.UsuarioEquipo;
import com.unir.teamapp.persist.repository.common.CustomJpaRepository;

@Repository("usuarioRepository")
public interface UsuarioRepository extends CustomJpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.rol r WHERE u.email = :username")
    Optional<Usuario> findByEmailWithRol(@Param("username") String username);

    boolean existsByEmail(String email);

}
