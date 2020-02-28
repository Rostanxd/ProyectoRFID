package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.concurrent.CopyOnWriteArrayList;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.control.ListItem;

public class PersistenceReceiveWare {
    private String DocOrigen;
    private String DocDestino;
    private String Motivo;
    private String OrdenCompra;
    private String GuiaEntrada;
    private CopyOnWriteArrayList<ListItem> ListEpcsLeidos;
    private CopyOnWriteArrayList<String> tagList;
    private int ListCycleCount;
    private String ItemsLeidos ;
    private String ItemsEsperados;
    private boolean EstadoBtnIniciar;
    private boolean EstadoBtnDetener;
    private boolean EstadoBtnLimpiar;
    private boolean EstadoBtnConfirmar;
    private EGDetailResponse egDetailResponse;

    public String getDocOrigen() {
        return DocOrigen;
    }

    public void setDocOrigen(String docOrigen) {
        DocOrigen = docOrigen;
    }

    public String getDocDestino() {
        return DocDestino;
    }

    public void setDocDestino(String docDestino) {
        DocDestino = docDestino;
    }

    public String getMotivo() {
        return Motivo;
    }

    public void setMotivo(String motivo) {
        Motivo = motivo;
    }

    public String getOrdenCompra() {
        return OrdenCompra;
    }

    public void setOrdenCompra(String ordenCompra) {
        OrdenCompra = ordenCompra;
    }

    public String getGuiaEntrada() {
        return GuiaEntrada;
    }

    public void setGuiaEntrada(String guiaEntrada) {
        GuiaEntrada = guiaEntrada;
    }

    public CopyOnWriteArrayList<ListItem> getListEpcsLeidos() {
        return ListEpcsLeidos;
    }

    public void setListEpcsLeidos(CopyOnWriteArrayList<ListItem> listEpcsLeidos) {
        ListEpcsLeidos = listEpcsLeidos;
    }

    public CopyOnWriteArrayList<String> getTagList() {
        return tagList;
    }

    public void setTagList(CopyOnWriteArrayList<String> tagList) {
        this.tagList = tagList;
    }

    public int getListCycleCount() {
        return ListCycleCount;
    }

    public void setListCycleCount(int listCycleCount) {
        ListCycleCount = listCycleCount;
    }

    public String getItemsLeidos() {
        return ItemsLeidos;
    }

    public void setItemsLeidos(String itemsLeidos) {
        ItemsLeidos = itemsLeidos;
    }

    public String getItemsEsperados() {
        return ItemsEsperados;
    }

    public void setItemsEsperados(String itemsEsperados) {
        ItemsEsperados = itemsEsperados;
    }

    public boolean isEstadoBtnIniciar() {
        return EstadoBtnIniciar;
    }

    public void setEstadoBtnIniciar(boolean estadoBtnIniciar) {
        EstadoBtnIniciar = estadoBtnIniciar;
    }

    public boolean isEstadoBtnDetener() {
        return EstadoBtnDetener;
    }

    public void setEstadoBtnDetener(boolean estadoBtnDetener) {
        EstadoBtnDetener = estadoBtnDetener;
    }

    public boolean isEstadoBtnLimpiar() {
        return EstadoBtnLimpiar;
    }

    public void setEstadoBtnLimpiar(boolean estadoBtnLimpiar) {
        EstadoBtnLimpiar = estadoBtnLimpiar;
    }

    public boolean isEstadoBtnConfirmar() {
        return EstadoBtnConfirmar;
    }

    public void setEstadoBtnConfirmar(boolean estadoBtnConfirmar) {
        EstadoBtnConfirmar = estadoBtnConfirmar;
    }

    public EGDetailResponse getEgDetailResponse() {
        return egDetailResponse;
    }

    public void setEgDetailResponse(EGDetailResponse egDetailResponse) {
        this.egDetailResponse = egDetailResponse;
    }
}
