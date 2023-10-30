package com.example.restyledonate;

public class MainCatalogos {
    String iddonacion, apellido, cantidad, correo, descripcion, horario, latitud, longitud, nombreArticulo, nombreCompleto, precio,telefono, tipoArticulo;
    String image, imagenUrl;

    MainCatalogos(){

    }

    public MainCatalogos(String iddonacion, String apellido, String cantidad, String correo, String descripcion, String horario, String latitud, String longitud, String nombreArticulo, String nombreCompleto, String precio, String telefono, String tipoArticulo, String image, String imagenUrl) {
        this.iddonacion = iddonacion;
        this.apellido = apellido;
        this.cantidad = cantidad;
        this.correo = correo;
        this.descripcion = descripcion;
        this.horario = horario;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombreArticulo = nombreArticulo;
        this.nombreCompleto = nombreCompleto;
        this.precio = precio;
        this.telefono = telefono;
        this.tipoArticulo = tipoArticulo;
        this.image = image;
        this.imagenUrl = imagenUrl;
    }

    public String getIddonacion() {
        return iddonacion;
    }

    public void setIddonacion(String iddonacion) {
        this.iddonacion = iddonacion;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getNombreArticulo() {
        return nombreArticulo;
    }

    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipoArticulo() {
        return tipoArticulo;
    }

    public void setTipoArticulo(String tipArticulo) {
        this.tipoArticulo = tipArticulo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
