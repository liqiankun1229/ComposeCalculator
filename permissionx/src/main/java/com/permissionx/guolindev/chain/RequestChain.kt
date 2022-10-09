/*
 * Copyright (C)  guolin, PermissionX Open Source Project
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
package com.permissionx.guolindev.chain

import com.permissionx.guolindev.task.BaseTask

/**
 * 维护权限请求流程的任务链
 */
class RequestChain {
    /**
     * 持有请求过程的第一个任务 权限请求从这里开始
     */
    private var headTask: BaseTask? = null

    /**
     * 持有请求过程的最后一个任务 权限请求到此结束
     */
    private var tailTask: BaseTask? = null

    /**
     * 将任务添加到任务链中
     * @param task  要添加的任务
     */
    internal fun addTaskToChain(task: BaseTask) {
        if (headTask == null) {
            headTask = task
        }
        // add task to the tail
        tailTask?.next = task
        tailTask = task
    }

    /**
     * 从第一个任务运行此任务链
     */
    internal fun runTask() {
        headTask?.request()
    }
}