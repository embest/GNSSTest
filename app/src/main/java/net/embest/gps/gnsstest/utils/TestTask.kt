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

class TestTask (var taskName: String, var argLong: Long, var argFloat: Float, var argString: String, var timeout: Long, var needBreak:Boolean) {
    var count: Int = 0
    fun test() {
        this.taskName = ""
        this.argLong = 0
        this.argFloat = 0f
        this.argString = ""
        this.needBreak = false
    }
}