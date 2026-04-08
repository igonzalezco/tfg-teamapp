package com.unir.teamapp.persist.repository.jpa;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.repository.common.CustomJpaRepository;
import com.unir.teamapp.persist.entity.Comentario;

@Repository("comentarioRepository")
public interface ComentarioRepository extends CustomJpaRepository<Comentario, Integer> {

}
