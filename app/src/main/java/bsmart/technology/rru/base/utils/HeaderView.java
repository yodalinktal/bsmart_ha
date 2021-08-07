package bsmart.technology.rru.base.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import androidx.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;

import bsmart.technology.rru.R;

public class HeaderView extends LinearLayout {

  private String leftString;

  private String titleString;

  private String rightString;

  private int rightRes;

  private int backgroundColor;

  private int textColorLeft = 0, textColorTitle = 0, textColorRight = 0;

  private FrameLayout mLeftFrame;

  private LinearLayout mTitleFrame;

  private FrameLayout mRightFrame;

  private TextView textViewRight;

  private int mSelectableResourceId;

  public HeaderView(Context context) {
    super(context);
    init();
  }

  public HeaderView(Context context, AttributeSet attrs) {
    super(context, attrs);

    TypedArray a = context.getTheme().obtainStyledAttributes(
        attrs,
        R.styleable.headerView,
        0, 0);

    try {
      leftString = a.getString(R.styleable.headerView_leftString);
      titleString = a.getString(R.styleable.headerView_titleString);
      rightString = a.getString(R.styleable.headerView_rightString);
      rightRes = a.getResourceId(R.styleable.headerView_rightRes, 0);

      int textColor = a.getColor(R.styleable.headerView_textColor, context.getResources().getColor(R.color.white));
      int buttonColor = a.getColor(R.styleable.headerView_textColor, context.getResources().getColor(R.color.white));

      textColorLeft = a.getColor(R.styleable.headerView_textColor, buttonColor);
      textColorTitle = a.getColor(R.styleable.headerView_textColor, textColor);
      textColorRight = a.getColor(R.styleable.headerView_textColor, buttonColor);

      backgroundColor = a.getColor(R.styleable.headerView_backgroundColor, context.getResources().getColor(R.color.colorPrimary));
    } finally {
      a.recycle();
    }

    TypedValue outValue = new TypedValue();
    getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
    mSelectableResourceId = outValue.resourceId;

    init();
  }

  public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    setOrientation(VERTICAL);
    setBackgroundColor(backgroundColor);
    Context context = getContext();
    LinearLayout mainLinear = new LinearLayout(context);
    mainLinear.setOrientation(HORIZONTAL);
    LayoutParams mainParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
    mainParams.weight = 1;
    addView(mainLinear, mainParams);
    if (backgroundColor == getResources().getColor(R.color.colorPrimary)) {

    }
    if (isInEditMode()) return;
    mainLinear.setGravity(Gravity.CENTER);

    View leftHolder = new View(context);
    mainLinear.addView(leftHolder, new ViewGroup.LayoutParams(ConvertUtils.dp2px(8), ViewGroup.LayoutParams.MATCH_PARENT));

    mLeftFrame = new FrameLayout(context);
    LayoutParams leftParams = new LayoutParams(ConvertUtils.dp2px(40), ConvertUtils.dp2px(40));
    mLeftFrame.setBackgroundResource(mSelectableResourceId);
    mLeftFrame.setVisibility(GONE);
    mainLinear.addView(mLeftFrame, leftParams);

    mTitleFrame = new LinearLayout(context);
    mTitleFrame.setOrientation(LinearLayout.VERTICAL);
    mTitleFrame.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
    LayoutParams titleParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
    titleParams.weight = 1;
    titleParams.leftMargin = ConvertUtils.dp2px(8);
    titleParams.rightMargin = ConvertUtils.dp2px(8);
    mainLinear.addView(mTitleFrame, titleParams);

    mRightFrame = new FrameLayout(context);
    LayoutParams rightParams = new LayoutParams(ConvertUtils.dp2px(40), ConvertUtils.dp2px(40));
    mRightFrame.setBackgroundResource(mSelectableResourceId);
    mRightFrame.setVisibility(GONE);
    mainLinear.addView(mRightFrame, rightParams);

    View rightHolder = new View(context);
    mainLinear.addView(rightHolder, new ViewGroup.LayoutParams(ConvertUtils.dp2px(8), ViewGroup.LayoutParams.MATCH_PARENT));

    if (leftString != null) setLeft(leftString, null);
    if (!TextUtils.isEmpty(titleString)) setTitle(titleString);
    if (!TextUtils.isEmpty(rightString)) setRight(rightString, null);
    if (rightRes > 0) setRight(rightRes, null);
  }

  public void setLeft(OnClickListener onClickListener) {
    setLeft(R.mipmap.left_c, "", onClickListener);
  }

  public void setLeft(String leftString, OnClickListener onClickListener) {
    setLeft(R.mipmap.left_c, leftString, onClickListener);
  }

  public void setLeft(@DrawableRes int drawableLeft, String leftString, OnClickListener onClickListener) {
    mLeftFrame.removeAllViews();

    ImageView backArrow = new ImageView(getContext());
    backArrow.setImageResource(drawableLeft);
    FrameLayout.LayoutParams backArrowParams = new FrameLayout.LayoutParams(ConvertUtils.dp2px(24), ConvertUtils.dp2px(24));
    backArrowParams.gravity = Gravity.CENTER;
    mLeftFrame.addView(backArrow, backArrowParams);
    if (onClickListener != null) {
      mLeftFrame.setOnClickListener(v -> onClickListener.onClick(v));
    }
    mLeftFrame.setVisibility(VISIBLE);
  }

  public void setLeftDefault(String leftString, OnClickListener onClickListener){
    setLeft(R.mipmap.icon_return, leftString, onClickListener);
  }

  public void setLeftDefault(OnClickListener onClickListener) {
    setLeft(R.mipmap.icon_return, "", onClickListener);
  }

  public void setLeftCustomView(View view) {
    mLeftFrame.removeAllViews();
    ViewGroup.LayoutParams leftFrameParams = mLeftFrame.getLayoutParams();
    leftFrameParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
    leftFrameParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
    mLeftFrame.requestLayout();
    mLeftFrame.addView(view);
    mLeftFrame.setVisibility(VISIBLE);
  }

  public void setRightCustomView(View view) {
    mRightFrame.removeAllViews();
    ViewGroup.LayoutParams rightFrameParams = mRightFrame.getLayoutParams();
    rightFrameParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
    rightFrameParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
    mRightFrame.requestLayout();
    mRightFrame.addView(view);
    mRightFrame.setVisibility(VISIBLE);
  }

  public void setRight(String rightString, OnClickListener onClickListener) {
    mRightFrame.removeAllViews();
    ViewGroup.LayoutParams rightFrameParams = mRightFrame.getLayoutParams();
    rightFrameParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
    rightFrameParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
    mRightFrame.requestLayout();

    TextView right = new TextView(getContext());
    right.setTextColor(textColorRight);
    right.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
    right.setGravity(Gravity.CENTER);
    right.setText(rightString);
    right.setPadding(ConvertUtils.dp2px(8), ConvertUtils.dp2px(8), ConvertUtils.dp2px(8), ConvertUtils.dp2px(8));
    FrameLayout.LayoutParams rightParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    rightParams.gravity = Gravity.CENTER;
    mRightFrame.addView(right, rightParams);
    if (onClickListener != null) {
      mRightFrame.setOnClickListener(v -> onClickListener.onClick(v));
    }
    mRightFrame.setVisibility(VISIBLE);
    textViewRight = right;
  }

  public ImageView setRight(@DrawableRes int rightRes, OnClickListener onClickListener) {
    mRightFrame.removeAllViews();

    ImageView imgAction = new ImageView(getContext());
    imgAction.setImageResource(rightRes);
    FrameLayout.LayoutParams backArrowParams = new FrameLayout.LayoutParams(ConvertUtils.dp2px(24), ConvertUtils.dp2px(24));
    backArrowParams.gravity = Gravity.CENTER;
    mRightFrame.addView(imgAction, backArrowParams);
    if (onClickListener != null) {
      mRightFrame.setOnClickListener(v -> onClickListener.onClick(v));
    }
    mRightFrame.setVisibility(VISIBLE);
    return imgAction;
  }

  public void setTitle(String titleString, String subTitleString) {
    mTitleFrame.removeAllViews();
    TextView title = new TextView(getContext());
    title.setTextColor(textColorTitle);
    title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
    title.setText(titleString);
    title.setGravity(Gravity.CENTER);
    title.setMaxLines(1);
    title.setTypeface(null, Typeface.BOLD);
    mTitleFrame.addView(title, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    TextView subTitle = new TextView(getContext());
    subTitle.setTextColor(textColorTitle);
    subTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
    subTitle.setText(subTitleString);
    subTitle.setGravity(Gravity.CENTER);
    subTitle.setMaxLines(1);
    LinearLayout.LayoutParams subTitleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    mTitleFrame.addView(subTitle, subTitleParams);
  }

  public void setSMTitle(String titleString, String subTitleString,int gravity) {
    mTitleFrame.setGravity(Gravity.CENTER_VERTICAL | gravity);
    mTitleFrame.removeAllViews();
    TextView title = new TextView(getContext());
    title.setTextColor(textColorTitle);
    title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    title.setText(titleString);
    title.setGravity(Gravity.CENTER);
    title.setMaxLines(1);
    title.setTypeface(null, Typeface.BOLD);
    mTitleFrame.addView(title, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    TextView subTitle = new TextView(getContext());
    subTitle.setTextColor(textColorTitle);
    subTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    subTitle.setText(subTitleString);
    subTitle.setGravity(Gravity.CENTER);
    subTitle.setMaxLines(1);
    LinearLayout.LayoutParams subTitleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    mTitleFrame.addView(subTitle, subTitleParams);
  }

  public void setTitle(String titleString) {
    mTitleFrame.removeAllViews();
    TextView title = new TextView(getContext());
    title.setTextColor(textColorTitle);
    title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
    title.setText(titleString);
    title.setGravity(Gravity.CENTER);
    title.setMaxLines(1);
    title.setTypeface(null, Typeface.BOLD);
    mTitleFrame.addView(title, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
  }

  public void setTitle_Main_Color(String titleString) {
    mTitleFrame.removeAllViews();
    TextView title = new TextView(getContext());
    title.setTextColor(backgroundColor);
    title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
    title.setText(titleString);
    title.setGravity(Gravity.CENTER);
    title.setMaxLines(1);
    title.setTypeface(null, Typeface.BOLD);
    mTitleFrame.addView(title, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
  }

  public void setTitle_Size(String titleString,int size) {
    mTitleFrame.removeAllViews();
    TextView title = new TextView(getContext());
    title.setTextColor(textColorTitle);
    title.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    title.setText(titleString);
    title.setGravity(Gravity.CENTER);
    title.setMaxLines(1);
    title.setTypeface(null, Typeface.BOLD);
    mTitleFrame.addView(title, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
  }

  public void setCustomTitleView(View view) {
    mTitleFrame.removeAllViews();
    mTitleFrame.addView(view, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
  }

  public TextView getTextViewRight() {
    return textViewRight;
  }
}

