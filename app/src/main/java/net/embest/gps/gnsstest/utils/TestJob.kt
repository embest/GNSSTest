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

import android.util.Log

class TestJob {
    var name = ""
    var round = 0L
    var cep = ""
    var longitude = 360.0
    var latitude = 360.0
    var count: Int = 0
        private set
    val tasks: ArrayList<TestTask> = ArrayList()

    val finished: Boolean
        get() = count >= round

    init {
        this.count = 0
        this.tasks.clear()
    }

    fun addTask(task: TestTask) {
        Log.e("TASK", "addTask:$task")
        this.tasks.add(task)
    }

    fun oneTestDone() {
        this.count = this.count + 1
    }

    fun cleanJob() {
        this.count = 0
        this.tasks.clear()
        this.round = 0
        this.name = ""
        this.cep = ""
        this.longitude = 360.0
        this.latitude = 360.0
    }
}