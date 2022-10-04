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
 *  Base DialogFragment class to inherits to display a rationale dialog and show user why you need the permissions that you asked.
 *  Your DialogFragment must have a positive button to proceed request and an optional negative button to cancel request. Override
 *  {@link RationaleDialogFragment#getPositiveButton()} and {@link RationaleDialogFragment#getNegativeButton()} to implement that.
 *  @author guolin
 *  @since 2020/9/1
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
     * Provide permissions to request. These permissions should be the ones that shows on your RationaleDialogFragment.
     * @return Permissions list to request.
     */
    abstract public @NonNull List<String> getPermissionsToRequest();

}