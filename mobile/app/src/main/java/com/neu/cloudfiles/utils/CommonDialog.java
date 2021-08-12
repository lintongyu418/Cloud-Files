package com.neu.cloudfiles.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import com.neu.cloudfiles.R;

import java.util.Calendar;
import java.util.Date;


/**
 * description:自定义dialog
 */
public class CommonDialog extends Dialog {

    private RadioGroup shareTypeRadioGroup;
    private DatePicker datePicker;
    private Button negativeBn, positiveBn;

    /**
     * 按钮之间的分割线
     */
    private View columnLineView;

    public CommonDialog(Context context) {
        super(context, R.style.CustomDialog);
    }

    /**
     * 都是内容数据
     */
    private String message;
    private String title;
    private String positive, negative;
    private int imageResId = -1;

    /**
     * 底部是否只有一个按钮
     */
    private boolean isSingle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_common);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面数据
        refreshView();
        //初始化界面控件的事件
        initEvent();
    }

    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        positiveBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener != null) {
                    onClickBottomListener.onPositiveClick(isPublicShare(), getDateFromDatePicker(datePicker));
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        negativeBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener != null) {
                    onClickBottomListener.onNegativeClick();
                }
            }
        });
    }

    private void refreshView() {
        //如果设置按钮的文字
        if (!TextUtils.isEmpty(positive)) {
            positiveBn.setText(positive);
        } else {
            positiveBn.setText("Share");
        }
        if (!TextUtils.isEmpty(negative)) {
            negativeBn.setText(negative);
        } else {
            negativeBn.setText("Cancel");
        }

        /**
         * 只显示一个按钮的时候隐藏取消按钮，回掉只执行确定的事件
         */
        if (isSingle) {
            columnLineView.setVisibility(View.GONE);
            negativeBn.setVisibility(View.GONE);
        } else {
            negativeBn.setVisibility(View.VISIBLE);
            columnLineView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void show() {
        super.show();
        refreshView();
    }

    private void initView() {
        negativeBn = (Button) findViewById(R.id.negative);
        positiveBn = (Button) findViewById(R.id.positive);
        shareTypeRadioGroup = (RadioGroup) findViewById(R.id.file_type_radio);
        columnLineView = findViewById(R.id.column_line);
        datePicker = (DatePicker) findViewById(R.id.date_picker);
    }

    private boolean isPublicShare() {
        return shareTypeRadioGroup.getCheckedRadioButtonId() == R.id.share_public;
    }

    public OnClickBottomListener onClickBottomListener;

    public CommonDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }

    public interface OnClickBottomListener {
        public void onPositiveClick(boolean publicShare, Date expireDate);

        public void onNegativeClick();

    }

    public static Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

}
