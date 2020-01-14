package co.kr.bluebird.newrfid.app.bbrfidbtdemo.control;

import java.util.List;

public class RfidAutentication {
    public static class RAEstado{
        public String codigo;
        public String mensaje;

        public String getCodigo() {
            return codigo;
        }

        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public RAEstado(String codigo, String mensaje) {
            this.codigo = codigo;
            this.mensaje = mensaje;
        }
    }
    public static class RAData{
        public RAUsuario usuario;
        public RARol rol;
        public RAAcceso acceso;

        public RAUsuario getUsuario() {
            return usuario;
        }

        public void setUsuario(RAUsuario usuario) {
            this.usuario = usuario;
        }

        public RARol getRol() {
            return rol;
        }

        public void setRol(RARol rol) {
            this.rol = rol;
        }

        public RAAcceso getAcceso() {
            return acceso;
        }

        public void setAcceso(RAAcceso acceso) {
            this.acceso = acceso;
        }

        public RAData(RAUsuario usuario, RARol rol, RAAcceso acceso) {
            this.usuario = usuario;
            this.rol = rol;
            this.acceso = acceso;
        }
    }
    public static class RAUsuario{
        public String id_;
        public String nombre;

        public String getId_() {
            return id_;
        }

        public void setId_(String id_) {
            this.id_ = id_;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public RAUsuario(String id_, String nombre) {
            this.id_ = id_;
            this.nombre = nombre;
        }
    }
    public static class RARol{
        public String id_;
        public String nombre;

        public String getId_() {
            return id_;
        }

        public void setId_(String id_) {
            this.id_ = id_;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public RARol(String id_, String nombre) {
            this.id_ = id_;
            this.nombre = nombre;
        }
    }
    public static class RAAcceso{
        public List<RAPrograma> programas;

        public List<RAPrograma> getProgramas() {
            return programas;
        }

        public void setProgramas(List<RAPrograma> programas) {
            this.programas = programas;
        }

        public RAAcceso(List<RAPrograma> programas) {
            this.programas = programas;
        }
    }
    public static class RAPrograma{
        public String nombre;
        public String ruta;

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getRuta() {
            return ruta;
        }

        public void setRuta(String ruta) {
            this.ruta = ruta;
        }

        public RAPrograma(String nombre, String ruta) {
            this.nombre = nombre;
            this.ruta = ruta;
        }
    }

    public RAEstado estado;
    public RAData data_;
    public RAEstado getEstado() {
        return estado;
    }
    public void setEstado(RAEstado estado) {
        this.estado = estado;
    }
    public RAData getData_() {
        return data_;
    }
    public void setData_(RAData data_) {
        this.data_ = data_;
    }

    public RfidAutentication(RAEstado estado, RAData data_) {
        this.estado = estado;
        this.data_ = data_;
    }
}

