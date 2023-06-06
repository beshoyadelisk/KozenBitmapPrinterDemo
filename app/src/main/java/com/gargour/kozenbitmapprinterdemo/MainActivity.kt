package com.gargour.kozenbitmapprinterdemo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.pos.sdk.printer.POIPrinterManager
import com.pos.sdk.printer.POIPrinterManager.IPrinterListener
import com.pos.sdk.printer.models.BitmapPrintLine
import com.pos.sdk.printer.models.PrintLine
import com.pos.sdk.printer.models.TextPrintLine


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var bitmap = BitmapFactory.decodeResource(resources, R.drawable.android_logo)
        bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.colorFilter = PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        findViewById<MaterialButton>(R.id.materialButton).setOnClickListener {
            printBitmap(bitmap)
        }
    }

    private fun printBitmap(bitmap: Bitmap) {
        val printerManager = POIPrinterManager(this)
        printerManager.open()
        val state = printerManager.printerState
        Log.d("printBitmap", "printer state = $state")
        if (state == 4) {
            printerManager.close()
            return
        }
        //printerManager.setPrintFont("/system/fonts/Android-1.ttf");
        printerManager.setPrintGray(5)
        printerManager.setLineSpace(5)
        printerManager.addPrintLine(BitmapPrintLine(bitmap, PrintLine.CENTER))
        printerManager.addPrintLine(TextPrintLine(" ", 0, 100))
        val listener: IPrinterListener = object : IPrinterListener {
            override fun onStart() {}
            override fun onFinish() {
                //printerManager.cleanCache();
                printerManager.close()
            }

            override fun onError(code: Int, msg: String) {
                Log.e("POIPrinterManager", "code: " + code + "msg: " + msg)
                printerManager.close()
            }
        }

        printerManager.beginPrint(listener)
    }
}