package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.io.Serializable;
import java.util.List;

public class EGTagsResponseItem implements Serializable {

    public String itemCodigo;
    public String itemGrupo1;
    public String itemGrupo2;
    public String itemGrupo3;
    public String itemGrupo4;
    public String itemGrupo5;
    public int CantidadLeidos;
    public int CantidadNoLeidos;
    public int CantidadDoc;

    public int getCantidadDoc() {
        return CantidadDoc;
    }

    public void setCantidadDoc(int cantidadDoc) {
        CantidadDoc = cantidadDoc;
    }

    public List<String> DataLeido;
    public List<String> DataNoLeido;

    public String getItemCodigo() {
        return itemCodigo;
    }

    public void setItemCodigo(String itemCodigo) {
        this.itemCodigo = itemCodigo;
    }

    public String getItemGrupo1() {
        return itemGrupo1;
    }

    public void setItemGrupo1(String itemGrupo1) {
        this.itemGrupo1 = itemGrupo1;
    }

    public String getItemGrupo2() {
        return itemGrupo2;
    }

    public void setItemGrupo2(String itemGrupo2) {
        this.itemGrupo2 = itemGrupo2;
    }

    public String getItemGrupo3() {
        return itemGrupo3;
    }

    public void setItemGrupo3(String itemGrupo3) {
        this.itemGrupo3 = itemGrupo3;
    }

    public String getItemGrupo4() {
        return itemGrupo4;
    }

    public void setItemGrupo4(String itemGrupo4) {
        this.itemGrupo4 = itemGrupo4;
    }

    public String getItemGrupo5() {
        return itemGrupo5;
    }

    public void setItemGrupo5(String itemGrupo5) {
        this.itemGrupo5 = itemGrupo5;
    }

    public int getCantidadLeidos() {
        return CantidadLeidos;
    }

    public void setCantidadLeidos(int cantidadLeidos) {
        CantidadLeidos = cantidadLeidos;
    }

    public int getCantidadNoLeidos() {
        return CantidadNoLeidos;
    }

    public void setCantidadNoLeidos(int cantidadNoLeidos) {
        CantidadNoLeidos = cantidadNoLeidos;
    }

    public List<String> getDataLeido() {
        return DataLeido;
    }

    public void setDataLeido(List<String> dataLeido) {
        DataLeido = dataLeido;
    }

    public List<String> getDataNoLeido() {
        return DataNoLeido;
    }

    public void setDataNoLeido(List<String> dataNoLeido) {
        DataNoLeido = dataNoLeido;
    }

    public EGTagsResponseItem(String itemCodigo, String itemGrupo1, String itemGrupo2, String itemGrupo3, String itemGrupo4, String itemGrupo5, int cantidadLeidos, int cantidadNoLeidos, List<String> dataLeido, List<String> dataNoLeido) {
        this.itemCodigo = itemCodigo;
        this.itemGrupo1 = itemGrupo1;
        this.itemGrupo2 = itemGrupo2;
        this.itemGrupo3 = itemGrupo3;
        this.itemGrupo4 = itemGrupo4;
        this.itemGrupo5 = itemGrupo5;
        CantidadLeidos = cantidadLeidos;
        CantidadNoLeidos = cantidadNoLeidos;
        DataLeido = dataLeido;
        DataNoLeido = dataNoLeido;
    }
}
