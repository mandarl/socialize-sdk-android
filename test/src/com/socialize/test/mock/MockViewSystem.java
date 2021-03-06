package com.socialize.test.mock;

import android.location.Location;
import com.socialize.api.SocializeSession;
import com.socialize.api.action.view.ViewSystem;
import com.socialize.entity.Entity;
import com.socialize.entity.View;
import com.socialize.listener.view.ViewListener;

public class MockViewSystem  extends MockSystem<View> implements ViewSystem {
	
	public MockViewSystem() {
		super(new View());
	}

	@Override
	public void addView(SocializeSession session, Entity entity, Location location, ViewListener listener) {
		action.setEntity(entity);
		if(listener != null) listener.onCreate(action);
	}

	@Override
	public void getViewsByEntity(SocializeSession session, String entityKey, int startIndex, int endIndex, ViewListener listener) {
		if(listener != null) listener.onList(actionList);
	}
	
	@Override
	public void getView(SocializeSession session, Entity entity, ViewListener listener) {
		if(listener != null) listener.onGet(action);
	}
	
	@Override
	public void getView(SocializeSession session, long id, ViewListener listener) {
		if(listener != null) listener.onGet(action);
	}

	@Override
	public void getViewsByUser(SocializeSession session, long userId, int startIndex, int endIndex, ViewListener listener) {
		if(listener != null) listener.onList(actionList);
	}
}
