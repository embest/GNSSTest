/*
 * Copyright (C) 2018 Embest
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.embest.gps.gnsstest.views

import net.embest.gps.gnsstest.R
import net.embest.gps.gnsstest.utils.GnssSatellite

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.graphics.*
import android.location.GnssStatus
import android.util.DisplayMetrics
import kotlin.math.abs

class SkyView : View {

    private var mSatellites: ArrayList<GnssSatellite>? = null

    private var mCircleX: Float = 0.0f
    private var mCircleY: Float = 0.0f
    private var mRadius: Float = 0.0f
    private var mIsPortrait:Boolean = true

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom - convertDpToPx(64)

        if (contentWidth < contentHeight){
            mCircleX = contentWidth/2.0f
            mCircleY = mCircleX + 10
            mRadius = contentWidth/2.0f -20
            mIsPortrait = true
        }
        else{
            mCircleX = contentHeight/2.0f
            mCircleY = mCircleX
            mRadius = contentHeight/2.0f -20
            mIsPortrait = false
        }

        onDrawBack(canvas,mIsPortrait)

        onDrawSatellite(canvas, mSatellites, mIsPortrait)
    }

    fun onUpDate(satellites: ArrayList<GnssSatellite>) {
        this.mSatellites = satellites
        invalidate()
    }

    private fun convertDpToPx(dp: Int): Int {
        return Math.round(dp * (resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))

    }

    private fun onDrawBack(canvas: Canvas, isPortrait:Boolean) {
        val paint = Paint()
        paint.color = context.getColor(R.color.colorPrimaryDark)
        paint.style = Paint.Style.FILL


        canvas.drawCircle(mCircleX, mCircleY, mRadius, paint)

        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.color = Color.GRAY
        val effects = DashPathEffect(floatArrayOf(5f, 5f, 5f, 5f), 1f)
        paint.pathEffect = effects
        canvas.drawCircle(mCircleX, mCircleY, mRadius, paint)
        canvas.drawCircle(mCircleX, mCircleY, mRadius/3, paint)
        canvas.drawCircle(mCircleX, mCircleY, mRadius/3*2, paint)
        canvas.drawLine(mCircleX-mRadius, mCircleY, mCircleX+mRadius, mCircleY, paint)
        canvas.drawLine(mCircleX, mCircleY-mRadius, mCircleX, mCircleY+mRadius, paint)

        paint.color = Color.GRAY
        paint.style = Paint.Style.FILL

        paint.textSize = mRadius/8
        canvas.drawText("N", (mCircleX - mRadius / 24), (mCircleY - mRadius + mRadius/8), paint)

        val infoBottom = mCircleY + mRadius

        paint.color = Color.BLACK
        paint.textSize = mRadius/12
        canvas.drawText("View", mCircleX-(mRadius/8)*7, infoBottom , paint)
        canvas.drawText("Use", mCircleX+(mRadius/8)*6, infoBottom, paint)

        if (isPortrait){
            var satBottom = mCircleY + mRadius + mRadius/12 + 50
            val div = mRadius/6

            val matrix = Matrix()
            matrix.postScale(0.5f, 0.5f)

            var bmp = BitmapFactory.decodeResource(resources, R.drawable.gps)
            var bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            canvas.drawBitmap(bitmap, mCircleX - div*5, satBottom, null)

            bmp = BitmapFactory.decodeResource(resources, R.drawable.galileo)
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            canvas.drawBitmap(bitmap, mCircleX - div*3, satBottom, null)

            bmp = BitmapFactory.decodeResource(resources, R.drawable.qzss)
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            canvas.drawBitmap(bitmap, mCircleX - div*1, satBottom, null)

            bmp = BitmapFactory.decodeResource(resources, R.drawable.glonass)
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            canvas.drawBitmap(bitmap, mCircleX + div*1, satBottom, null)

            bmp = BitmapFactory.decodeResource(resources, R.drawable.beidou)
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            canvas.drawBitmap(bitmap, mCircleX + div*3, satBottom, null)

            satBottom = mCircleY + mRadius + mRadius/12 + 150

            bmp = BitmapFactory.decodeResource(resources, R.drawable.gps_df)
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            canvas.drawBitmap(bitmap, mCircleX - div*5, satBottom, null)

            bmp = BitmapFactory.decodeResource(resources, R.drawable.galileo_df)
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            canvas.drawBitmap(bitmap, mCircleX - div*3, satBottom, null)

            bmp = BitmapFactory.decodeResource(resources, R.drawable.qzss_df)
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            canvas.drawBitmap(bitmap, mCircleX - div*1, satBottom, null)

            bmp = BitmapFactory.decodeResource(resources, R.drawable.irnss_df)
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            canvas.drawBitmap(bitmap, mCircleX + div*1, satBottom, null)

            bmp = BitmapFactory.decodeResource(resources, R.drawable.un)
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            canvas.drawBitmap(bitmap, mCircleX + div*3, satBottom, null)



            val snrBottom = satBottom + 200
            val textSize = mRadius/12
            val snrDiv = mCircleX/4

            paint.color = Color.BLACK
            paint.textSize = textSize
            canvas.drawText("00", snrDiv * 1, snrBottom + textSize, paint)
            canvas.drawText("10", snrDiv * 2, snrBottom + textSize, paint)
            canvas.drawText("20", snrDiv * 3, snrBottom + textSize, paint)
            canvas.drawText("30", snrDiv * 4, snrBottom + textSize, paint)
            canvas.drawText("40", snrDiv * 5, snrBottom + textSize, paint)
            canvas.drawText("99", snrDiv * 6, snrBottom + textSize, paint)
//
            val snrTop = snrBottom - 50
            paint.color = getColor(2, true)
            canvas.drawRect(snrDiv * 1, snrTop, snrDiv * 2, snrBottom, paint)
            paint.color = getColor(12, true)
            canvas.drawRect(snrDiv * 2, snrTop, snrDiv * 3, snrBottom, paint)
            paint.color = getColor(22, true)
            canvas.drawRect(snrDiv * 3, snrTop, snrDiv * 4, snrBottom, paint)
            paint.color = getColor(32, true)
            canvas.drawRect(snrDiv * 4, snrTop, snrDiv * 5, snrBottom, paint)
            paint.color = getColor(42, true)
            canvas.drawRect(snrDiv * 5, snrTop, snrDiv * 7, snrBottom, paint)
        }
        else{
            var satLeft = mCircleX + mRadius + mRadius/12 + 150
            val div = mRadius/6

            val matrix = Matrix()
            matrix.postScale(0.5f, 0.5f)

            var bmp = BitmapFactory.decodeResource(resources, R.drawable.gps)
            var bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            canvas.drawBitmap(bitmap, satLeft, mCircleY-div*4, null)

            bmp = BitmapFactory.decodeResource(resources, R.drawable.galileo)
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            canvas.drawBitmap(bitmap, satLeft, mCircleY-div*2, null)

            bmp = BitmapFactory.decodeResource(resources, R.drawable.qzss)
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            canvas.drawBitmap(bitmap, satLeft, mCircleY-div*0, null)

            bmp = BitmapFactory.decodeResource(resources, R.drawable.glonass)
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            canvas.drawBitmap(bitmap, satLeft, mCircleY+div*2, null)

            bmp = BitmapFactory.decodeResource(resources, R.drawable.beidou)
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            canvas.drawBitmap(bitmap, satLeft, mCircleY+div*4, null)


            satLeft += 300

            bmp = BitmapFactory.decodeResource(resources, R.drawable.gps_df)
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            canvas.drawBitmap(bitmap, satLeft, mCircleY-div*4, null)

            bmp = BitmapFactory.decodeResource(resources, R.drawable.galileo_df)
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            canvas.drawBitmap(bitmap, satLeft, mCircleY-div*2, null)

            bmp = BitmapFactory.decodeResource(resources, R.drawable.qzss_df)
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            canvas.drawBitmap(bitmap, satLeft, mCircleY-div*0, null)

            bmp = BitmapFactory.decodeResource(resources, R.drawable.irnss_df)
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            canvas.drawBitmap(bitmap, satLeft, mCircleY+div*2, null)

            bmp = BitmapFactory.decodeResource(resources, R.drawable.un)
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            canvas.drawBitmap(bitmap, satLeft, mCircleY+div*4, null)

            var snrLeft = satLeft + 400
            val snrY = mCircleY/4

            paint.color = Color.BLACK
            paint.textSize = mRadius/12
            canvas.drawText("00", snrLeft, snrY*7, paint)
            canvas.drawText("10", snrLeft, snrY*6, paint)
            canvas.drawText("20", snrLeft, snrY*5, paint)
            canvas.drawText("30", snrLeft, snrY*4, paint)
            canvas.drawText("40", snrLeft, snrY*3, paint)
            canvas.drawText("99", snrLeft, snrY*2, paint)
//
            snrLeft -=  50
            val snrRight = snrLeft+50
            paint.color = getColor(2, true)
            canvas.drawRect(snrLeft, snrY*7, snrRight, snrY*6, paint)
            paint.color = getColor(12, true)
            canvas.drawRect(snrLeft, snrY*6, snrRight, snrY*5, paint)
            paint.color = getColor(22, true)
            canvas.drawRect(snrLeft, snrY*5, snrRight, snrY*4, paint)
            paint.color = getColor(32, true)
            canvas.drawRect(snrLeft, snrY*4, snrRight, snrY*3, paint)
            paint.color = getColor(42, true)
            canvas.drawRect(snrLeft, snrY*3, snrRight, snrY*1, paint)
        }
    }

    private fun getColor(snr: Int, used: Boolean): Int {
        if (used){
            return when {
                snr < 10 -> Color.RED
                snr < 20 -> -0xb86e0
                snr < 30 -> -0x2c00
                snr < 40 -> -0x4d2dcb
                else -> -0xff0100}
        }
        return Color.GRAY
    }

    private fun degreeToRadian(degree: Double): Double {
        return degree * Math.PI / 180.0
    }

    private fun onDrawSatellite(canvas: Canvas, satellites: ArrayList<GnssSatellite>?, isPortrait: Boolean) {
        val paint = Paint()

        if (satellites != null) {
            var inUse = 0
            var gps = 0
            var glo = 0
            var qzss = 0
            var bds = 0
            var gal = 0

            var gps_df = 0
            var irnss = 0
            var qzss_df = 0
            var un = 0
            var gal_df = 0

            for (satellite in satellites) {

                val r2 = mRadius * ((90.0f - satellite.elevations) / 90.0f)
                val radian = degreeToRadian(360.0 - satellite.azimuths + 90)

                val x = mCircleX - Math.cos(radian) * r2
                val y = mCircleY - Math.sin(radian) * r2

                paint.style = Paint.Style.FILL
                paint.color = getColor(satellite.cn0.toInt(), satellite.inUse)

                var bmp = BitmapFactory.decodeResource(resources, R.drawable.un)

                when (satellite.constellation) {
                    GnssStatus.CONSTELLATION_GPS -> {
                        if (abs(satellite.frequency - GnssSatellite.GPS_L5_FREQUENCY) < 200.0){
                            bmp =  BitmapFactory.decodeResource(resources, R.drawable.gps_df)
                            gps_df += 1
                        }else {
                            bmp = BitmapFactory.decodeResource(resources, R.drawable.gps)
                            gps += 1
                        }
                    }
                    GnssStatus.CONSTELLATION_GLONASS -> {
                        bmp = BitmapFactory.decodeResource(resources, R.drawable.glonass)
                        glo += 1
                    }
                    GnssStatus.CONSTELLATION_QZSS -> {
                        if (abs(satellite.frequency - GnssSatellite.QZSS_L5_FREQUENCY) < 200.0){
                            bmp = BitmapFactory.decodeResource(resources, R.drawable.qzss_df)
                            qzss_df += 1
                        }else{
                            bmp = BitmapFactory.decodeResource(resources, R.drawable.qzss)
                            qzss += 1
                        }
                    }
                    GnssStatus.CONSTELLATION_BEIDOU -> {
                        bmp = BitmapFactory.decodeResource(resources, R.drawable.beidou)
                        bds += 1
                    }
                    GnssStatus.CONSTELLATION_GALILEO -> {
                        if (abs(satellite.frequency - GnssSatellite.GAL_L5_FREQUENCY) < 200.0){
                            bmp = BitmapFactory.decodeResource(resources, R.drawable.galileo_df)
                            gal_df += 1
                        }else{
                            bmp = BitmapFactory.decodeResource(resources, R.drawable.galileo)
                            gal += 1
                        }
                    }
                    7 -> {
                        bmp = BitmapFactory.decodeResource(resources, R.drawable.irnss_df)
                        irnss += 1
                    }
                    else ->{
                        un += 1
                    }
                }

                val matrix = Matrix()
                matrix.postScale(0.4f, 0.4f)
                val bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
                canvas.drawBitmap(bitmap, x.toFloat(), y.toFloat(), null)
                paint.color = getColor(satellite.cn0.toInt(), satellite.inUse)
                paint.textSize = mRadius/10
                canvas.drawText("" + satellite.svid, x.toFloat() + mSatelliteR, y.toFloat(), paint)
                if(satellite.inUse){
                    inUse += 1
                }
            }

            if(isPortrait){
                val infoBottom = mCircleY + mRadius

                paint.color = Color.BLACK
                paint.textSize = mRadius/8
                canvas.drawText(satellites.size.toString(), mCircleX-(mRadius/8)*7, infoBottom - 50, paint)
                canvas.drawText(inUse.toString(), mCircleX+(mRadius/8)*6, infoBottom - 50, paint)

                var satBottom = mCircleY + mRadius + mRadius/6 + mRadius/12
                val div = mRadius/6
                paint.textSize = mRadius/12
                canvas.drawText(gps.toString(),  mCircleX - div*4, satBottom, paint)
                canvas.drawText(gal.toString(),  mCircleX - div*2, satBottom, paint)
                canvas.drawText(qzss.toString(), mCircleX - div*0, satBottom, paint)
                canvas.drawText(glo.toString(),  mCircleX + div*2, satBottom, paint)
                canvas.drawText(bds.toString(),  mCircleX + div*4, satBottom, paint)

                satBottom += 100
                canvas.drawText(gps_df.toString(),  mCircleX - div*4, satBottom, paint)
                canvas.drawText(gal_df.toString(),  mCircleX - div*2, satBottom, paint)
                canvas.drawText(qzss_df.toString(), mCircleX - div*0, satBottom, paint)
                canvas.drawText(irnss.toString(),   mCircleX + div*2, satBottom, paint)
                canvas.drawText(un.toString(),      mCircleX + div*4, satBottom, paint)
            }
            else{
                val infoBottom = mCircleY + mRadius

                paint.color = Color.BLACK
                paint.textSize = mRadius/8
                canvas.drawText(satellites.size.toString(), mCircleX-(mRadius/8)*7, infoBottom - 50, paint)
                canvas.drawText(inUse.toString(), mCircleX+(mRadius/8)*6, infoBottom - 50, paint)

                var satLeft = mCircleX + mRadius + mRadius/12 + 300
                val div = mRadius/6
                paint.textSize = mRadius/8
                canvas.drawText(gps.toString(),  satLeft,  mCircleY-div*4+mRadius/8, paint)
                canvas.drawText(gal.toString(),  satLeft,  mCircleY-div*2+mRadius/8, paint)
                canvas.drawText(qzss.toString(), satLeft,  mCircleY-div*0+mRadius/8, paint)
                canvas.drawText(glo.toString(),  satLeft,  mCircleY+div*2+mRadius/8, paint)
                canvas.drawText(bds.toString(),  satLeft,  mCircleY+div*4+mRadius/8, paint)


                satLeft += 300
                canvas.drawText(gps_df.toString(),  satLeft,  mCircleY-div*4+mRadius/8, paint)
                canvas.drawText(gal_df.toString(),  satLeft,  mCircleY-div*2+mRadius/8, paint)
                canvas.drawText(qzss_df.toString(), satLeft,  mCircleY-div*0+mRadius/8, paint)
                canvas.drawText(irnss.toString(),   satLeft,  mCircleY+div*2+mRadius/8, paint)
                canvas.drawText(un.toString(),      satLeft,  mCircleY+div*4+mRadius/8, paint)
            }
        }
    }

    companion object {

        private var mSatelliteR = 10

    }
}