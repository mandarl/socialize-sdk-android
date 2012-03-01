package com.socialize.ui.auth;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socialize.ui.util.Colors;
import com.socialize.ui.view.ClickableSectionCell;

public class AnonymousCell extends ClickableSectionCell {

	public AnonymousCell(Context context) {
		super(context);
	}
	
	public void init() {
		setCanClick(false);
		setStrokeCornerOffset(1);
		super.init();
	}

	@Override
	protected ImageView makeImage() {
		ImageView view = new ImageView(getContext());
		view.setImageDrawable(drawables.getDrawable("user_icon.png#no_density", DisplayMetrics.DENSITY_DEFAULT));
		return view;
	}

	@Override
	protected View makeDisplayText() {
		
		LinearLayout layout = new LinearLayout(getContext());
		layout.setOrientation(VERTICAL);
		
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		
		TextView title = new TextView(getContext());
		TextView sub = new TextView(getContext());
		
		title.setText("You are currently anonymous");
		sub.setText("Authenticate with a service above");
		
		title.setTextColor(Colors.parseColor("#97a6b1"));
		sub.setTextColor(Colors.parseColor("#6e7b84"));
		
		title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		sub.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
	
		layout.setLayoutParams(params);
		
		layout.addView(title);
		layout.addView(sub);
		
		return layout;
	}
}
