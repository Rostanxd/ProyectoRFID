package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

import java.util.ArrayList;
import java.util.List;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGDetailResponse;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGProcesado;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGTagsResponseItem;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.TagNoRead;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.item;

public class RfidEpHomologacion {

    public EGProcesado DetailResponseToEGProcesado(EGDetailResponse detailResponse){
        EGProcesado egProcesado = new EGProcesado(null);
        item _item = null;
        List<item> itemList = new ArrayList<item>();
        TagNoRead tagNoRead = null ;
        List<String> taglectura = null;
        int totalcant = 0;

        for (EGTagsResponseItem i :detailResponse.getItems()) {

            taglectura = new ArrayList<String>();

            for (String j :i.getDataNoLeido()) {
                taglectura.add(j);
            }
            tagNoRead = new TagNoRead(taglectura);
            _item = new item(i.itemCodigo,(i.CantidadLeidos + i.CantidadNoLeidos),i.CantidadLeidos,tagNoRead);
            itemList.add(_item);

            totalcant += _item.getCantidad();
        }
        egProcesado = new EGProcesado(itemList,totalcant);

        return egProcesado;
    }
}
