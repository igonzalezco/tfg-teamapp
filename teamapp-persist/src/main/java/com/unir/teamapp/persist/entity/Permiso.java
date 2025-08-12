package com.unir.teamapp.persist.entity;

import java.io.Serial;
import java.io.Serializable;

import com.unir.teamapp.persist.annotation.UniqueEntity;
import com.unir.teamapp.persist.util.FieldConstants;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "permiso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@UniqueEntity(fields = FieldConstants.CODIGO)
public class Permiso extends BaseEntity implements Serializable, BaseEntityId<Integer> {

    @Serial
    private static final long serialVersionUID = 7669856742523510860L;

    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "permiso_id_generator", sequenceName = "sq_permiso_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permiso_id_generator")
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "codigo")
    private String codigo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "prioridad")
    private Integer prioridad;
}
