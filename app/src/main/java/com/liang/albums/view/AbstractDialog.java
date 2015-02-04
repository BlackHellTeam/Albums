package com.liang.albums.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.liang.albums.R;

public abstract class AbstractDialog extends Dialog implements View.OnClickListener {

	private DialogInterface.OnClickListener mOnClickListener;

	public AbstractDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public AbstractDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public DialogInterface.OnClickListener getOnClickListener() {
		return mOnClickListener;
	}

	public AbstractDialog setOnClickListener(DialogInterface.OnClickListener onClickListener) {
		mOnClickListener = onClickListener;
		return this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		onCreateDialog();
		if (mOnClickListener != null) {
			if (findOkButton() != null)
				findOkButton().setOnClickListener(this);
			if (findCancelButton() != null)
				findCancelButton().setOnClickListener(this);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (dismissDialogOnClick()) AbstractDialog.this.dismiss();
		switch (v.getId()) {
		case R.id.btn_close:
			mOnClickListener.onClick(AbstractDialog.this, Dialog.BUTTON_POSITIVE);
			break;
		case R.id.btn_done:
			mOnClickListener.onClick(AbstractDialog.this, Dialog.BUTTON_NEGATIVE);
			break;
		}
	}

	protected boolean dismissDialogOnClick() {
		return true;
	}

	protected abstract void onCreateDialog();
	protected abstract Button findOkButton();
	protected abstract Button findCancelButton();
	
}
