package com.unir.teamapp.persist.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.repository.common.CustomJpaRepository;
import com.unir.teamapp.persist.entity.UsuarioEquipo;

@Repository("usuarioEquipoRepository")
public interface UsuarioEquipoRepository extends CustomJpaRepository<UsuarioEquipo, Integer> {

  @Query("SELECT ue FROM UsuarioEquipo ue JOIN FETCH ue.equipo WHERE ue.usuario.id = :id")
  List<UsuarioEquipo> findByUsuarioId(Integer id);

}
