
package bsmart.technology.rru.base.utils;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;

import bsmart.technology.rru.R;


public class TabMainItemView extends FrameLayout {
  public TabMainItemView(@NonNull Context context) {
    super(context);
    init();
  }

  public TabMainItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public TabMainItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private TextView mText, mBadge;

  private ImageView mIcon;

  private int mTextColorSelected, mTextColorNormal;

  private String mStr;


  private int dp16 = ConvertUtils.dp2px(16);
  private int dp10 = ConvertUtils.dp2px(10);

  private
  @DrawableRes
  int mResDrawableNormal, mResDrawableSelected;

  private void init() {
    LayoutInflater.from(getContext()).inflate(R.layout.item_tab_main, this, true);
    mText = (TextView) findViewById(R.id.text);
    mBadge = (TextView) findViewById(R.id.badge);
    mIcon = (ImageView) findViewById(R.id.icon);
    Resources mResources = getContext().getResources();
    mTextColorSelected = mResources.getColor(R.color.colorPrimary);
    mTextColorNormal = mResources.getColor(R.color.textCommentSecond);
  }

  public void setResource(@DrawableRes int resDrawableNormal,
                          @DrawableRes int resDrawableSelected,
                          String str) {
    this.mResDrawableNormal = resDrawableNormal;
    this.mResDrawableSelected = resDrawableSelected;
    this.mStr = str;
    mIcon.setImageResource(mResDrawableNormal);
    mText.setText(str);
  }

  public void onSelected() {
    mIcon.setImageResource(mResDrawableSelected);
    mText.setTextColor(mTextColorSelected);
  }

  public void onUnSelected() {
    mIcon.setImageResource(mResDrawableNormal);
    mText.setTextColor(mTextColorNormal);
  }

  public void showNumber(Integer aInt) {
    ViewGroup.LayoutParams layoutParams = mBadge.getLayoutParams();
    layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
    layoutParams.height = dp16;
    mBadge.requestLayout();
    String str = aInt > 99 ? "99+" : (aInt + "");
    mBadge.setText(str);
    mBadge.setVisibility(VISIBLE);
  }

  public void removeShow() {
    mBadge.setText("");
    mBadge.setVisibility(GONE);
  }

  public void showDot() {
    ViewGroup.LayoutParams layoutParams = mBadge.getLayoutParams();
    layoutParams.width = dp10;
    layoutParams.height = dp10;
    mBadge.requestLayout();
    mBadge.setText("");
    mBadge.setVisibility(VISIBLE);
  }
}

