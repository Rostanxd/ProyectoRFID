package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

public class DataSourceDto {
    public String codigo;
    public String descripcion;
    public String auxiliar;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAuxiliar() {
        return auxiliar;
    }

    public void setAuxiliar(String auxiliar) {
        this.auxiliar = auxiliar;
    }

    public DataSourceDto(String codigo, String descripcion, String auxiliar) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.auxiliar = auxiliar;
    }


    @Override
    public String toString() {
        return descripcion;
    }
}
