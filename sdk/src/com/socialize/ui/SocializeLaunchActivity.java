/*
 * Copyright (c) 2011 Socialize Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.socialize.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.socialize.Socialize;
import com.socialize.SocializeService;
import com.socialize.android.ioc.IOCContainer;
import com.socialize.api.SocializeSession;
import com.socialize.config.SocializeConfig;
import com.socialize.error.SocializeErrorHandler;
import com.socialize.error.SocializeException;
import com.socialize.launcher.LaunchManager;
import com.socialize.launcher.Launcher;
import com.socialize.listener.SocializeAuthListener;

/**
 * Generic launcher activity.
 * @author Jason Polites
 */
public class SocializeLaunchActivity extends Activity {

	public static final String LAUNCH_ACTION = "socialize.launch.action";
	
	protected IOCContainer container;
	protected Launcher launcher;
	protected SocializeErrorHandler errorHandler;
	protected Intent originalIntent;
	
	@Override
	protected void onNewIntent(Intent intent) {
		originalIntent = intent;
		super.onNewIntent(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		superOnCreate(savedInstanceState);
		
		originalIntent = getIntent();
		
		RelativeLayout layout = new RelativeLayout(this);
		
		layout.setBackgroundColor(Color.BLACK);
		
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		
		TextView text = new TextView(this);
		text.setText("Loading...");
		text.setTextColor(Color.WHITE);
		
		LayoutParams text_params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		text_params.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		text.setLayoutParams(text_params);
		
		layout.addView(text);
		
		layout.setLayoutParams(params);
		
		setContentView(layout);
		
		new Initializer().execute((Void[])null);
	}
	
	protected void doInit() {
		initSocialize();
		container = getContainer();
		errorHandler = container.getBean("socializeUIErrorHandler");
	}
	
	protected void doAuthenticate() {
		// Authenticate the user
		getSocialize().authenticate(this, getAuthListener(container));
	}
	
	protected SocializeAuthListener getAuthListener(final IOCContainer container) {
		
		return new SocializeAuthListener() {
			
			@Override
			public void onError(SocializeException error) {
				handleError(error);
			}
			
			@Override
			public void onAuthFail(SocializeException error) {
				handleError(error);
			}			
			
			@Override
			public void onCancel() {
				finish();
			}
			
			@Override
			public void onAuthSuccess(SocializeSession session) {
				Bundle extras = getIntent().getExtras();
				if(extras != null) {
					String action = extras.getString(LAUNCH_ACTION);
					if(action != null) {
						LaunchManager launchManager = container.getBean("launchManager");
						if(launchManager != null) {
							launcher = launchManager.getLaucher(action);
							if(launcher != null) {
								if(launcher.launch(SocializeLaunchActivity.this, extras)) {
									if(!launcher.shouldFinish()) {
										return; // Don't finish
									}
								}
							}
						}
					}
				}	
				
				finish();
			}
		};
	}
	
	protected void handleError(SocializeException error) {
		error.printStackTrace();
		if(errorHandler != null) {
			errorHandler.handleError(SocializeLaunchActivity.this, error);
		}		
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(launcher != null) {
			launcher.onResult(this, requestCode, resultCode, data, originalIntent);
		}
		finish();
	}

	protected void superOnCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected IOCContainer getContainer() {
		return ActivityIOCProvider.getInstance().getContainer();
	}
	
	protected String getConsumerKey(IOCContainer container) {
		return getSocialize().getConfig().getProperty(SocializeConfig.SOCIALIZE_CONSUMER_KEY);
	}
	
	protected String getConsumerSecret(IOCContainer container) {
		return getSocialize().getConfig().getProperty(SocializeConfig.SOCIALIZE_CONSUMER_SECRET);
	}
	
	protected String getFacebookAppId(IOCContainer container) {
		return getSocialize().getConfig().getProperty(SocializeConfig.FACEBOOK_APP_ID);
	}
	
	protected void initSocialize() {
		getSocialize().init(this);
	}
	
	protected SocializeService getSocialize() {
		return Socialize.getSocialize();
	}
	
	protected class Initializer extends AsyncTask<Void, Void, Void>  {

		@Override
		protected Void doInBackground(Void... arg0) {
			doInit();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			doAuthenticate();
		}
	}
}
