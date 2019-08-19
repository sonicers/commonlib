package com.sonicers.commonlib.component;

import android.content.Context;
import android.os.Bundle;

import com.sonicers.commonlib.i.IFragmentHandler;
import com.trello.rxlifecycle3.components.RxFragment;

public class BaseFragment extends RxFragment {
    protected static final String ARG_PARAM1 = "tag";
    protected String mTag;
    protected Context mContext;
    private IFragmentHandler mIFragmentHandler;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentHandler) {
            mIFragmentHandler = (IFragmentHandler) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement mIFragmentHandler");
        }
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTag = getArguments().getString(ARG_PARAM1);
        }
    }

    public String getCustomTag() {
        return mTag;
    }

    public boolean onBackPressed() {
        if (mIFragmentHandler.isFragmentVisible(mTag)) {
            mIFragmentHandler.removeFragment(mTag);
            return true;//截取返回键
        }
        return false;//不响应返回键
    }
}
