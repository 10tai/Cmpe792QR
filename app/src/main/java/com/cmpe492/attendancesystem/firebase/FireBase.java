/**
 * Copyright 2016 Erik Jhordan Rey.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cmpe492.attendancesystem.firebase;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cmpe492.attendancesystem.AppGlobals;
import com.firebase.client.Firebase;

public class FireBase {

  // url of firebase database from firebase console
  private static final String FIRE_BASE_URL = "https://attendance-system-9e021.firebaseio.com/";
  // First main Child of the database
  private static final String FIRE_BASE_CHILD = "Attendance";

  private static Firebase firebase;

  private FireBase() {
  }

  // this constructor returns the Firebase with the First base child of database.
  public static Firebase getInstance(@NonNull Context context) {
    Firebase.setAndroidContext(context);
    if (firebase == null) firebase = new Firebase(FIRE_BASE_URL).child(FIRE_BASE_CHILD);

    return firebase;
  }
}
