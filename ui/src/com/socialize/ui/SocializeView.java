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

import android.content.Context;
import android.util.AttributeSet;

import com.socialize.Socialize;
import com.socialize.android.ioc.IOCContainer;
import com.socialize.ui.error.SocializeUIErrorHandler;

/**
 * @author Jason Polites
 */
public abstract class SocializeView extends BaseView {

	protected IOCContainer container;
	protected Context context;
	
	public SocializeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public SocializeView(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		onBeforeSocializeInit();
		initSocialize();
		container = ActivityIOCProvider.getInstance().getContainer();
		setErrorHandler((SocializeUIErrorHandler) container.getBean("socializeUIErrorHandler"));
		onPostSocializeInit(container);
	}
	
	protected <E extends Object> E getBean(String name) {
		return container.getBean(name);
	}

	protected void initSocialize() {
		Socialize.init(this.getContext());
	}
	
	// Subclasses override
	protected void onBeforeSocializeInit() {}
	
	protected void onPostSocializeInit(IOCContainer container) {}
}
