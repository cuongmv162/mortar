/*
 * Copyright 2013 Square Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.mortar.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import mortar.Mortar;
import mortar.MortarScope;
import mortar.Popup;

public class ConfirmerPopup implements Popup<Confirmation, Boolean> {
  private final Context context;
  private final Listener<Boolean> listener;

  private AlertDialog dialog;

  public ConfirmerPopup(Context context, Listener<Boolean> listener) {
    this.context = context;
    this.listener = listener;
  }

  @Override public MortarScope getMortarScope() {
    return Mortar.getScope(context);
  }

  @Override
  public void show(Confirmation info, boolean withFlourish) {
    if (dialog != null) throw new IllegalStateException("Already showing, can't show " + info);

    dialog = new AlertDialog.Builder(context).setTitle(info.title)
        .setMessage(info.body)
        .setPositiveButton(info.confirm, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface d, int which) {
            dialog = null;
            listener.onDismissed(true);
          }
        })
        .setNegativeButton(info.cancel, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface d, int which) {
            dialog = null;
            listener.onDismissed(false);
          }
        })
        .setCancelable(true)
        .setOnCancelListener(new DialogInterface.OnCancelListener() {
          @Override public void onCancel(DialogInterface d) {
            dialog = null;
            listener.onDismissed(false);
          }
        })
        .show();
  }

  @Override public boolean isShowing() {
    return dialog != null;
  }

  @Override public void dismiss(boolean withFlourish) {
    dialog.dismiss();
    dialog = null;
  }
}