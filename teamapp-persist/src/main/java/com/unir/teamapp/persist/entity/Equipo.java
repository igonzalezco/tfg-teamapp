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
@Table(name = "equipo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class Equipo extends AuditableEntity implements Serializable, BaseEntityId<Integer> {

    @Serial
    private static final long serialVersionUID = 7669856984223510860L;

    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "equipo_id_generator", sequenceName = "sq_equipo_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "equipo_id_generator")
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(cascade= CascadeType.ALL, mappedBy = "equipo", fetch = FetchType.LAZY)
    private List<Jugador> jugadores;

    @OneToMany(cascade= CascadeType.ALL, mappedBy = "equipo", fetch = FetchType.LAZY)
    private List<Cuota> cuotas;

    @OneToMany(cascade= CascadeType.ALL, mappedBy = "equipo", fetch = FetchType.LAZY)
    private List<Noticia> noticias;

    @OneToMany(cascade= CascadeType.ALL, mappedBy = "equipo", fetch = FetchType.LAZY)
    private List<Encuesta> encuestas;
}
