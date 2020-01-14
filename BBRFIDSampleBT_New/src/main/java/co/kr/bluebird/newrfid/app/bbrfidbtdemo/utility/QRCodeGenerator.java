package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRCodeGenerator {

    public Bitmap QrCodePrint(String cadena)
    {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Bitmap bitmap = null;

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(cadena, BarcodeFormat.QR_CODE,300,300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);

        }
        catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
