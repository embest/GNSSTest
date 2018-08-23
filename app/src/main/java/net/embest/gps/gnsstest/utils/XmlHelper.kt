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

package net.embest.gps.gnsstest.utils

import android.os.Environment
import java.io.File
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import android.util.Xml
import java.io.FileInputStream


class XmlHelper {

    private val mExternalPath = EXTERNAL_PATH + "GNSSTest/config"

    private val mTestJob = TestJob()

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(file:String): TestJob {
        mTestJob.cleanJob()
        mTestJob.name = file.substring(0,file.length-4)
        val initialFile = File(mExternalPath,file)
        val inputStream = FileInputStream(initialFile)
        inputStream.use {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(it, null)
            parser.nextTag()
            readTest(parser)
            return mTestJob
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readTest(parser: XmlPullParser) {
        parser.require(XmlPullParser.START_TAG, ns, "test")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            when (name) {
                "task" -> {
                    mTestJob.addTask(readTask(parser))
                }
                "round" -> {
                    mTestJob.round = readLong(parser, "round")
                }
                "cep" -> {
                    mTestJob.cep = readString(parser ,"cep")
                }
                "longitude" -> {
                    mTestJob.longitude = readString(parser, "longitude").toDouble()
                }
                "latitude" -> {
                    mTestJob.latitude = readString(parser, "latitude").toDouble()
                }
                else -> skip(parser)
            }
        }
    }


    @Throws(XmlPullParserException::class, IOException::class)
    private fun readTask(parser: XmlPullParser): TestTask {
        parser.require(XmlPullParser.START_TAG, ns, "task")
        val task = TestTask("",0,0f,"",0,true)

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            when (name) {
                "name" -> task.taskName= readString(parser, name)
                "long" -> task.argLong = readLong(parser, name)
                "float" -> task.argFloat = readFloat(parser, name)
                "string" -> task.argString = readString(parser, name)
                "timeout" -> task.timeout = readLong(parser, name)
                "break" -> task.needBreak = readBoolean(parser ,name)
                else -> skip(parser)
            }
        }
        return task
    }



    @Throws(IOException::class, XmlPullParserException::class)
    private fun readLong(parser: XmlPullParser, tag:String): Long {
        parser.require(XmlPullParser.START_TAG, ns, tag)
        val text = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, tag)
        return text.toLong()
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readFloat(parser: XmlPullParser, tag:String): Float {
        parser.require(XmlPullParser.START_TAG, ns, tag)
        val text = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, tag)
        return text.toFloat()
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readString(parser: XmlPullParser, tag:String): String {
        parser.require(XmlPullParser.START_TAG, ns, tag)
        val text = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, tag)
        return text
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readBoolean(parser: XmlPullParser, tag:String): Boolean {
        parser.require(XmlPullParser.START_TAG, ns, tag)
        val text = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, tag)
        return text.toBoolean()
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }

    companion object {
        //private const val TAG = "GNSSTestXml"
        private val ns: String? = null
        private val EXTERNAL_PATH = Environment.getExternalStorageDirectory().path + "/"
    }
}