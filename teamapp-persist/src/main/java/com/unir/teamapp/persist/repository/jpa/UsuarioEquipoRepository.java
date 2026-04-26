package com.unir.teamapp.persist.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.entity.UsuarioEquipo;
import com.unir.teamapp.persist.repository.common.CustomJpaRepository;

@Repository("usuarioEquipoRepository")
public interface UsuarioEquipoRepository extends CustomJpaRepository<UsuarioEquipo, Integer> {

  @Query("SELECT ue FROM UsuarioEquipo ue JOIN FETCH ue.equipo e JOIN FETCH ue.permiso p WHERE ue.usuario.id = :id AND e.deletedAt IS NULL")
  List<UsuarioEquipo> findByUsuarioId(Integer id);

  @Query("SELECT ue FROM UsuarioEquipo ue WHERE ue.equipo.id = :id")
  List<UsuarioEquipo> findByEquipoId(Integer id);

  Optional<UsuarioEquipo> findByUsuarioIdAndEquipoId(Integer usuarioId, Integer equipoId);
}
