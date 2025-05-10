package com.unir.teamapp.persist.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "noticia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class Noticia extends AuditableEntity implements Serializable, BaseEntityId<Integer> {

    @Serial
    private static final long serialVersionUID = 7632556357829874860L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "noticia_seq")
    @SequenceGenerator(name = "noticia_seq", sequenceName = "noticia_seq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "titulo")
    private String titulo;

    @Column(name = "contenido")
    private String contenido;

    @JoinColumn(name = "equipo", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Equipo equipo;

    @OneToMany(cascade= CascadeType.ALL, mappedBy = "cuota", fetch = FetchType.LAZY)
    private List<Comentario> comentarios;

}
