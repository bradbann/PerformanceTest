/*
 * Copyright (c) 2012-2013 NetEase, Inc. and other contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package zengweicong.com.performancetest.utils;

import android.app.Application;
import android.view.WindowManager;

import java.io.File;

/**
 * my application class
 */
public class MyApplication extends Application {

	private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

	public WindowManager.LayoutParams getMywmParams() {
		return wmParams;
	}

	@Override
	public void onCreate() {
		initAppConfig();
		super.onCreate();
	}
	
	private void initAppConfig() {
		// create directory of Performace
		File dir = new File(zengweicong.com.performancetest.utils.Settings.Performance_RESULT_DIR);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
	
}
