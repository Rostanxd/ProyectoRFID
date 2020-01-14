package co.kr.bluebird.newrfid.app.bbrfidbtdemo.models;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.control.EntryGuide;
import java.util.ArrayList;
import java.util.List;

public class EntryGuideModel {
    public List<EntryGuide> findAll()
    {
        List<EntryGuide> products = new ArrayList<EntryGuide>();
        products.add(new EntryGuide("1","2500","Procesado"));
        products.add(new EntryGuide("2","2780","Procesado"));
        products.add(new EntryGuide("3","2500","Pendiente"));
        products.add(new EntryGuide("4","2780","Procesado"));


        return  products;
    }

    public EntryGuide find(String id)
    {
        for(EntryGuide entryGuide : this.findAll())
        {
            if(entryGuide.getNum_entrada().equalsIgnoreCase(id))
            {
                return  entryGuide;
            }
        }
        return  null;
    }
}
