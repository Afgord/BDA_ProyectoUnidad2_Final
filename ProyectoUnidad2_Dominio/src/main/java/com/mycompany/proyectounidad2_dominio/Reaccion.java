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
 *
 * @author Afgord
 */
@Entity
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

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoReaccion tipo;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_emisor", nullable = false)
    private Estudiante emisor;

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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

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

    @Override
    public String toString() {
        return "Reaccion{" + "id=" + id + ", tipo=" + tipo + ", fecha=" + fecha + '}';
    }

}
