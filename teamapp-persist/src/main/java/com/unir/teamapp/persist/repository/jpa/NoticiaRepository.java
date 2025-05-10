package com.unir.teamapp.persist.repository.jpa;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.repository.common.ComplexJpaRepository;
import com.unir.teamapp.persist.entity.Noticia;

@Repository("noticiaRepository")
public interface NoticiaRepository extends ComplexJpaRepository<Noticia, Integer> {

}
