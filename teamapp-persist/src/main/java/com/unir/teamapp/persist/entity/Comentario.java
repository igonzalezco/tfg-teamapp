package com.unir.teamapp.persist.entity;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "comentario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class Comentario extends AuditableEntity implements Serializable, BaseEntityId<Integer> {

    @Serial
    private static final long serialVersionUID = 7676324157829874860L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comentario_seq")
    @SequenceGenerator(name = "comentario_seq", sequenceName = "comentario_seq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "texto")
    private String texto;

    @JoinColumn(name = "noticia", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Noticia noticia;

    @JoinColumn(name = "usuario", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario usuario;

}
