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

package com.permissionx.guolindev.dialog;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.List;

/**
 *  继承的基本 DialogFragment 类以显示基本原理对话框并向用户展示您为什么需要您要求的权限
 *  您的 DialogFragment 必须有一个肯定按钮来继续请求和一个可选的否定按钮来取消请求
 *  覆盖 getPositiveButton() 和 getNegativeButton() 来实现它
 */
public abstract class RationaleDialogFragment extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            dismiss();
        }
    }

    /**
     * 获取确认按钮控件
     */
    abstract public @NonNull View getPositiveButton();

    /**
     * 获取取消按钮控件
     */
    abstract public @Nullable View getNegativeButton();

    /**
     * 提供请求的权限 这些权限应该是显示在您的 RationaleDialogFragment 上的权限
     * @return 要请求的权限列表
     */
    abstract public @NonNull List<String> getPermissionsToRequest();

}