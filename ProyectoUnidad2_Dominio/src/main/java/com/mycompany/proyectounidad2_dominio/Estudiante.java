/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectounidad2_dominio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Afgord
 */
@Entity
@Table(name = "estudiante")
public class Estudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudiante")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name = "ap_pat", nullable = false)
    private String apPat;
    @Column(name = "ap_mat", nullable = false)
    private String apMat;
    @Column(name = "correo_inst", nullable = false, unique = true)
    private String correoInst;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "carrera", nullable = false)
    private String carrera;
    @Column(name = "foto_perfil")
    private String fotoPerfil;
    @Column(name = "descripcion", length = 500)
    private String descripcion;
    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    @ManyToMany
    @JoinTable(
            name = "estudiante_hobby",
            joinColumns = @JoinColumn(name = "id_estudiante"),
            inverseJoinColumns = @JoinColumn(name = "id_hobby")
    )
    private Set<Hobby> hobbies = new HashSet<>();

    public Estudiante() {
    }

    public Estudiante(String nombre, String apPat, String apMat, String correoInst,
            String password, String carrera, String fotoPerfil, String descripcion) {
        this.nombre = nombre;
        this.apPat = apPat;
        this.apMat = apMat;
        this.correoInst = correoInst;
        this.password = password;
        this.carrera = carrera;
        this.fotoPerfil = fotoPerfil;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApPat() {
        return apPat;
    }

    public void setApPat(String apPat) {
        this.apPat = apPat;
    }

    public String getApMat() {
        return apMat;
    }

    public void setApMat(String apMat) {
        this.apMat = apMat;
    }

    public String getCorreoInst() {
        return correoInst;
    }

    public void setCorreoInst(String correoInst) {
        this.correoInst = correoInst;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Set<Hobby> getHobbies() {
        return hobbies;
    }

    public void setHobbies(Set<Hobby> hobbies) {
        this.hobbies = hobbies;
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
        if (!(object instanceof Estudiante)) {
            return false;
        }
        Estudiante other = (Estudiante) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Estudiante{" + "id=" + id + ", nombre=" + nombre + ", apPat=" + apPat + ", apMat=" + apMat + ", correoInst="
                + correoInst + ", carrera=" + carrera + ", fotoPerfil=" + fotoPerfil + ", descripcion=" + descripcion + ", activo=" + activo + '}';
    }

}
