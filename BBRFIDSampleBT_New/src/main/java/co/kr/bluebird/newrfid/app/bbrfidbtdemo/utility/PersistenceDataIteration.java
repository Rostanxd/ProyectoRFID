package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DataSourceDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.LoginData;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.LoginProgram;

public class PersistenceDataIteration {

    private Context mContext;

    public PersistenceDataIteration(Context context) {
        mContext = context;
    }

    public LoginData LoginDataPersistence(){

        LoginData loginData = new LoginData();

        SharedPreferences prefs = mContext.getSharedPreferences("shared_login_data",   Context.MODE_PRIVATE);

        Set<String> accesos = prefs.getStringSet("accesos",null);
        List<LoginProgram> programList = new ArrayList<LoginProgram>();
        LoginProgram program =null;
        String separador = Pattern.quote("|");


        for (String x:accesos) {

            program  =new LoginProgram();
            String[] Array_access = x.split(separador);
            program.setNombre(Array_access[0]);
            program.setRuta(Array_access[1]);

            programList.add(program);

        }

        loginData.setUsuario(new DataSourceDto(prefs.getString("userid", ""), prefs.getString("username", ""), null));
        loginData.setRol(new DataSourceDto(prefs.getString("rolid", ""), prefs.getString("rolname", ""), null));
        loginData.setAccesos(programList);

        return loginData;

    }
}
