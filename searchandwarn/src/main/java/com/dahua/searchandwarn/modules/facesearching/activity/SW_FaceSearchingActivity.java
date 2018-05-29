package com.dahua.searchandwarn.modules.facesearching.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dahua.searchandwarn.R;
import com.dahua.searchandwarn.base.SW_Constracts;
import com.dahua.searchandwarn.model.SW_DeviceCodeBean;
import com.dahua.searchandwarn.model.SW_FaceCropBean;
import com.dahua.searchandwarn.model.SW_FaceParams;
import com.dahua.searchandwarn.model.SW_UserLoginBean;
import com.dahua.searchandwarn.modules.warning.activity.SW_TreeActivity;
import com.dahua.searchandwarn.net.SW_RestfulApi;
import com.dahua.searchandwarn.net.SW_RestfulClient;
import com.dahua.searchandwarn.utils.Base64FileUtils;
import com.dahua.searchandwarn.utils.LogUtils;
import com.dahua.searchandwarn.utils.TimeConstants;
import com.dahua.searchandwarn.utils.TimeUtils;
import com.dahua.searchandwarn.utils.ToastUtils;
import com.lvfq.pickerview.DSSTimePickerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SW_FaceSearchingActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_CAMERA = 101;
    public static final int REQUEST_ALBUM = 102;
    private ImageView ivBack;
    private ImageView ivFace;
    private LinearLayout llFace;
    private TextView tvTitle;
    private EditText etSimilarity;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvSite;
    private TextView tvSure;
    private String strStratTime;
    private String strEndTime;
    private Uri imageUri;
    private File file;
    private RecyclerView rv;
    private List<SW_FaceCropBean.DataBean> list;
    private boolean onBind;
    private String imgUrl;
    private CompositeDisposable compositeDisposable;
    private Intent intent;
    private String deviceCodes;
    private String endPath = Environment.getExternalStorageDirectory() + "/img.jpeg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sw_activity_face_searching);
        EventBus.getDefault().register(this);
        initView();

        tvTitle.setText("人脸检索");
        tvStartTime.setText(strStratTime);
        tvEndTime.setText(strEndTime);
        etSimilarity.setText("85");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(linearLayoutManager);


    }


    private void initView() {
        intent = new Intent(this, SW_FaceSearchingResultActivity.class);
        compositeDisposable = new CompositeDisposable();
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivFace = (ImageView) findViewById(R.id.iv_face);
        llFace = (LinearLayout) findViewById(R.id.ll_face);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        etSimilarity = (EditText) findViewById(R.id.et_similarity);
        tvStartTime = (TextView) findViewById(R.id.tv_start_time);
        tvEndTime = (TextView) findViewById(R.id.tv_end_time);
        tvSite = (TextView) findViewById(R.id.tv_site);
        tvSure = (TextView) findViewById(R.id.tv_sure);
        rv = (RecyclerView) findViewById(R.id.rv);
        Date date = TimeUtils.getDateByNow(get30DayMillis(), TimeConstants.MSEC);
        strStratTime = TimeUtils.date2String(date, new SimpleDateFormat("YYYY-MM-dd 00:00:00"));
        strEndTime = TimeUtils.getNowString(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss"));
        ivBack.setOnClickListener(this);
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        tvSite.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        llFace.setOnClickListener(this);
        ivFace.setOnClickListener(this);
        etSimilarity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etSimilarity.setText("");
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.iv_back) {
            finish();
        } else if (i == R.id.iv_face) {
            showPopupWindow();
        } else if (i == R.id.ll_face) {
            showPopupWindow();

        } else if (i == R.id.tv_start_time) {
            getStartTimePicker(tvStartTime);
        } else if (i == R.id.tv_end_time) {
            getEndStartTimePicker(tvEndTime);
        } else if (i == R.id.tv_site) {
            startActivity(new Intent(this, SW_TreeActivity.class));
        } else if (i == R.id.tv_sure) {
            imgUrl = null;
            if (list!=null){
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).isChecked()) {
                        imgUrl = list.get(j).getSmallImgBase64();
                    }
                }
            }
            String startTime = tvStartTime.getText().toString().trim();
            String endTime = tvEndTime.getText().toString().trim();
            String similarity = etSimilarity.getText().toString().trim();
            SW_FaceParams sw_faceParams = new SW_FaceParams();
            sw_faceParams.setStartTime(startTime);
            sw_faceParams.setEndTime(endTime);
            if (deviceCodes == null) {
                sw_faceParams.setDeviceCodes("-1");
            } else {
                sw_faceParams.setDeviceCodes(deviceCodes);
            }
            if (!TextUtils.isEmpty(imgUrl)) {
                sw_faceParams.setImageBase64(imgUrl);
            }
            sw_faceParams.setSimilarity(similarity);
            sw_faceParams.setOperator("xzm");
            EventBus.getDefault().postSticky(sw_faceParams);
            startActivity(intent);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMood(SW_DeviceCodeBean bean) {
        deviceCodes = bean.getDevCode();
        String address = bean.getAddress();
        tvSite.setText(address);
        LogUtils.e(deviceCodes);
    }

    private void showPopupWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.sw_view_popup_photo, null);
        TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
        TextView tv_photo = (TextView) view.findViewById(R.id.tv_photo);
        TextView tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        //拍照
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //只调用系统相机
                final Intent intent_camera = getApplicationContext().getPackageManager()
                        .getLaunchIntentForPackage("com.android.camera");
                if (intent_camera != null) {
                    cameraIntent.setPackage("com.android.camera");
                }

                cameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                imageUri = getImageUrl();
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
            }
        });
        tv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                albumIntent.setType("image/*");
                startActivityForResult(albumIntent, REQUEST_ALBUM);
            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    if (file != null) {
                        try {
                            llFace.setVisibility(View.GONE);
                            ivFace.setVisibility(View.VISIBLE);
                            String path = file.getAbsolutePath();
                            Bitmap bitmap = BitmapFactory.decodeFile(path);
                            FileOutputStream os = new FileOutputStream(endPath);

                            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, os);
                            ivFace.setImageBitmap(bitmap);
                            getPic(endPath);
                        } catch (Exception ex) {
                        }

                    }
                } else {
                    llFace.setVisibility(View.VISIBLE);
                    ivFace.setVisibility(View.GONE);
                }

                break;
            case REQUEST_ALBUM:
                ContentResolver resolver = getContentResolver();
                try {
                    //小米手机路径转换
                    String path = "";
                    if (data != null) {
                        Uri uri = data.getData();
/*
                        if (!TextUtils.isEmpty(uri.getAuthority())) {
                            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null) {
                                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                                if (cursor.moveToFirst()) {
                                    path = cursor.getString(index);
                                    cursor.close();
                                }
                            }
                        } else {
                            path = uri.getPath();
                        }*/

                        InputStream inputStream = resolver.openInputStream(uri);
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        FileOutputStream os = new FileOutputStream(endPath);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, os);

                        ivFace.setImageBitmap(bitmap);
                        llFace.setVisibility(View.GONE);
                        ivFace.setVisibility(View.VISIBLE);
                        LogUtils.e(endPath);
                        getPic(endPath);
                    }
                } catch (FileNotFoundException e) {


                    e.printStackTrace();
                }
                break;
        }
    }

    private void getPic(String path) {
        final String encode = Base64FileUtils.fileToBase64(path);
        final SW_RestfulApi restfulApi = SW_RestfulClient.getInstance().getRestfulApi(SW_Constracts.getBaseUrl(this));
        restfulApi.userLogin(SW_UserLoginBean.USERNANE, SW_UserLoginBean.PASSWORD)
                .flatMap(new Function<SW_UserLoginBean, ObservableSource<SW_FaceCropBean>>() {
                    @Override
                    public ObservableSource<SW_FaceCropBean> apply(SW_UserLoginBean sw_userLoginBean) throws Exception {
                        if (sw_userLoginBean.getRetCode() == 0) {
                            return restfulApi.getFaceCrop("data:image/jpeg;base64," + encode);
                        } else {
                            return null;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SW_FaceCropBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(SW_FaceCropBean sw_faceCropBean) {
                        if (sw_faceCropBean.getRetCode() == 0) {
                            list = sw_faceCropBean.getData();
                            if (list.size() == 0) {
                                ToastUtils.showLong("未检测到人脸");
                            }
                            for (int i = 0; i < list.size(); i++) {
                                if (i == 0) {
                                    list.get(i).setChecked(true);
                                } else {
                                    list.get(i).setChecked(false);
                                }
                            }
                            rv.setAdapter(new BaseQuickAdapter<SW_FaceCropBean.DataBean, BaseViewHolder>(R.layout.sw_item_choose_face, list) {

                                @Override
                                protected void convert(BaseViewHolder helper, final SW_FaceCropBean.DataBean item) {
                                    onBind = true;
                                    final int adapterPosition = helper.getAdapterPosition();
                                    Bitmap bitmap = decode(item.getSmallImgBase64());
                                    helper.setImageBitmap(R.id.iv_face, bitmap);
                                    final CheckBox cb = helper.getView(R.id.cb);
                                    cb.setChecked(item.isChecked());
                                    onBind = false;
                                    cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                            if (!onBind) {
                                                if (b) {
                                                    for (int i = 0; i < list.size(); i++) {
                                                        if (i == adapterPosition) {
                                                            list.get(i).setChecked(true);
                                                        } else {
                                                            list.get(i).setChecked(false);
                                                        }
                                                    }
                                                    notifyDataSetChanged();
                                                }else {
                                                    for (int i = 0; i < list.size(); i++) {
                                                        list.get(i).setChecked(false);
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                        }
                        LogUtils.e("onNext");
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showLong("人脸检测失败");
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.e("onComplete");
                    }
                });
    }

    private File createFile(Context context) {
        File file;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String timeStamp = String.valueOf(System.currentTimeMillis());
            file = new File(Environment.getExternalStorageDirectory() +
                    File.separator + timeStamp + ".jpg");
        } else {
            File cacheDir = context.getCacheDir();
            String timeStamp = String.valueOf(System.currentTimeMillis());
            file = new File(cacheDir, timeStamp + ".jpg");
        }
        return file;
    }

    private Uri getImageUrl() {
        file = createFile(this.getApplicationContext());
        Uri imgUri = Uri.fromFile(file);
        return imgUri;
    }


    private void getStartTimePicker(final TextView tvTime) {
        DSSTimePickerView pickerView = new DSSTimePickerView(this, DSSTimePickerView.Type.ALL);
        pickerView.show();
        pickerView.setOnTimeSelectListener(new DSSTimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                tvTime.setText(TimeUtils.date2String(date, new SimpleDateFormat("YYYY-MM-dd HH:mm:ss")));
            }
        });

    }

    private void getEndStartTimePicker(final TextView tvTime) {
        DSSTimePickerView pickerView = new DSSTimePickerView(this, DSSTimePickerView.Type.ALL);
        pickerView.show();
        pickerView.setOnTimeSelectListener(new DSSTimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                tvTime.setText(TimeUtils.date2String(date, new SimpleDateFormat("YYYY-MM-dd HH:mm:ss")));
            }
        });

    }

    private long get30DayMillis() {

        return -30 * 24 * 60 * 60 * 1000l;
    }

    //隐藏软键盘同时使EditText失去焦点
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    assert v != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    v.clearFocus();
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    //图片转换成base64
    private String encode(String path) {
        //decode to bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        //convert to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, baos);
        byte[] bytes = baos.toByteArray();

        //base64 encode
        byte[] encode = Base64.encode(bytes, Base64.DEFAULT);
        String encodeString = new String(encode);
        return encodeString;
    }

    private Bitmap decode(String path) {
        byte[] bytes = Base64.decode(path, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        compositeDisposable.dispose();
    }
}
