/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectounidad2_dominio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Representa una reacción entre dos estudiantes dentro del sistema.
 *
 * Una reacción modela la interacción de un estudiante emisor hacia un
 * estudiante receptor, pudiendo ser de tipo LIKE o NO_INTERESA.
 *
 * Características: - Relación recursiva hacia la entidad Estudiante -
 * Restricción única por par emisor-receptor - Registro de fecha de la reacción
 *
 * Esta entidad es la base para la generación automática de matches cuando
 * existe interés mutuo entre dos estudiantes.
 *
 * @author Afgord
 */
@Entity
/**
 * Se define una restricción única sobre (id_emisor, id_receptor) para evitar
 * múltiples reacciones del mismo estudiante hacia el mismo receptor.
 */
@Table(name = "reaccion",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"id_emisor", "id_receptor"})
        })
public class Reaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reaccion")
    private Long id;

    /**
     * Tipo de reacción emitida por el estudiante.
     *
     * Se almacena como texto en base de datos para mejorar legibilidad y evitar
     * dependencia del orden ordinal del enum.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoReaccion tipo;

    /**
     * Fecha en la que se registró o actualizó la reacción.
     */
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    /**
     * Estudiante que emite la reacción.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_emisor", nullable = false)
    private Estudiante emisor;

    /**
     * Estudiante que recibe la reacción.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_receptor", nullable = false)
    private Estudiante receptor;

    public Reaccion() {
    }

    public Reaccion(TipoReaccion tipo, LocalDate fecha, Estudiante emisor, Estudiante receptor) {
        this.tipo = tipo;
        this.fecha = fecha;
        this.emisor = emisor;
        this.receptor = receptor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoReaccion getTipo() {
        return tipo;
    }

    public void setTipo(TipoReaccion tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Estudiante getEmisor() {
        return emisor;
    }

    public void setEmisor(Estudiante emisor) {
        this.emisor = emisor;
    }

    public Estudiante getReceptor() {
        return receptor;
    }

    public void setReceptor(Estudiante receptor) {
        this.receptor = receptor;
    }

    /**
     * Calcula el hash basado en el identificador de la reacción.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Compara dos reacciones basándose en su identificador.
     *
     * Nota: Este método depende de que el ID esté asignado, por lo que puede no
     * comportarse como se espera en entidades no persistidas.
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reaccion)) {
            return false;
        }
        Reaccion other = (Reaccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Representación en cadena de la reacción.
     *
     * Incluye únicamente datos generales de identificación y omite referencias
     * completas a emisor y receptor para evitar salidas extensas o recursivas.
     */
    @Override
    public String toString() {
        return "Reaccion{" + "id=" + id + ", tipo=" + tipo + ", fecha=" + fecha + '}';
    }

}
