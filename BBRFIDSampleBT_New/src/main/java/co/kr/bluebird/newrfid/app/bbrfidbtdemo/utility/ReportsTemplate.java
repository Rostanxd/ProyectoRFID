package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.QrData;

public class ReportsTemplate {

    public String PlantillaHtmlQR(QrData qrData){

        String html = "<html>\n" +
                "<head>\n" +
                "\t<title></title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t<h5 style='margin: 5px;'>"+qrData.getContenidoQr()+"</h5>\n" +
                "\t<h5 style='margin: 5px;'>"+qrData.getMotivo()+"</h5>\n" +
                "\t<h5 style='margin: 5px;'>Realizado por &nbsp"+qrData.getUsuario()+"</h5>\n" +
                "\t\n" +
                "\t<table>\n" +
                "\t\t<tr>\n" +
                "\t\t\t<td><img src='{IMAGE_PLACEHOLDER}' /></td>\n" +
                "\t\t\t<td>\n" +
                "\t\t\t\t<div align=\"center\">\n" +
                "\t\t\t\t\t<font size=5>"+qrData.getCantidad()+"</font>\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t\t<div>\n" +
                "\t\t\t\t\t<font size=5>Unidad(es)</font>\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t</td>\n" +
                "\t\t</tr>\n" +
                "\t\t\n" +
                "\t</table>\n" +
                "</body>\n" +
                "</html>";

        html = html.replace("{IMAGE_PLACEHOLDER}", qrData.getImageQrBase64());

        return html;

    }
}
