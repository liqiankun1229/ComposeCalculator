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

package com.permissionx.guolindev.request;

import java.util.List;

/**
 * 定义一个任务接口来请求权限
 * 并非一次可以请求所有权限
 * 有些权限需要单独申请 所以每个权限请求都需要实现这个接口，并在其实现中做请求逻辑
 */
public interface ChainTask {

    /**
     * 获取用于显示 RequestReasonDialog 的 ExplainScope。
     * @return ExplainScope 的实例
     */
    ExplainScope getExplainScope();

    /**
     * 获取 ForwardScope 以显示 ForwardToSettingsDialog
     * @return ForwardScope 实例
     */
    ForwardScope getForwardScope();

    /**
     * 做请求逻辑
     */
    void request();

    /**
     * 当用户被拒绝时再次请求权限
     * @param permissions - 再次请求的权限
     */
    void requestAgain(List<String> permissions);

    /**
     * 完成此任务并通知请求结果
     */
    void finish();
}