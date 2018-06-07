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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dahua.searchandwarn.R;
import com.dahua.searchandwarn.base.LoadingDialogUtils;
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
import com.lvfq.pickerview.TimePickerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
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

        etSimilarity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSimilarity.setText("");
            }
        });
        etSimilarity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int max = 100;
                int min =0;
                if (start >= 0) {//从一输入就开始判断，
                    if (min != -1 && max != -1) {
                        try {
                            int num = Integer.parseInt(s.toString());
                            //判断当前edittext中的数字(可能一开始Edittext中有数字)是否大于max
                            if (num > max) {
                                s = String.valueOf(max);//如果大于max，则内容为max
                                etSimilarity.setText(s);
                            } else if (num < min) {
                                s = String.valueOf(min);//如果小于min,则内容为min
                            }
                        } catch (NumberFormatException e) {

                        }
                        //edittext中的数字在max和min之间，则不做处理，正常显示即可。
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
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
            if (imgUrl == null) {
                ToastUtils.showShort("请选择人脸");
                return;
            }
            if (tvSite.getText().toString().equals(getString(R.string.choose))) {
                ToastUtils.showShort("请选择抓拍地点");
                return;
            }
            if (deviceCodes == null) {
                sw_faceParams.setDeviceCodes("-1");
            } else {
                sw_faceParams.setDeviceCodes(deviceCodes);
            }
            if (!TextUtils.isEmpty(imgUrl)) {
                sw_faceParams.setImageBase64(imgUrl);
            }
            sw_faceParams.setSimilarity(similarity);
            sw_faceParams.setOperator(SW_UserLoginBean.USERNANE);
            EventBus.getDefault().postSticky(sw_faceParams);
            startActivity(intent);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMood(SW_DeviceCodeBean bean) {
        if (TextUtils.isEmpty(bean.getAddress())){
            tvSite.setText(R.string.choose);
        }else {
            tvSite.setText(bean.getAddress());
        }
        deviceCodes = bean.getDevCode();
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
                            Glide.with(SW_FaceSearchingActivity.this).load(file).into(ivFace);
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
        LoadingDialogUtils.show(SW_FaceSearchingActivity.this);
        final String encode = Base64FileUtils.fileToBase64(path);
        final SW_RestfulApi restfulApi = SW_RestfulClient.getInstance().getRestfulApi(SW_Constracts.getBaseUrl(this));
        restfulApi.getFaceCrop("data:image/jpeg;base64," + encode).subscribeOn(Schedulers.io())
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
                                ToastUtils.showLong("获取人脸失败");
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
                                    final ImageView iv_img = helper.getView(R.id.iv_face);
                                    iv_img.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (cb.isChecked()){
                                                cb.setChecked(false);
                                            }else {
                                                cb.setChecked(true);
                                            }
                                        }
                                    });
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
                        }else {
                            ToastUtils.showLong(sw_faceCropBean.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingDialogUtils.dismiss();
                        LogUtils.e("onError:"+e.getMessage());
                        ToastUtils.showLong("获取人脸失败");
                    }

                    @Override
                    public void onComplete() {
                        LoadingDialogUtils.dismiss();
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
        final String etime = tvEndTime.getText().toString();
        TimePickerView pickerView = new TimePickerView(this, TimePickerView.Type.ALL);
        pickerView.show();
        pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                Date date1 = TimeUtils.string2Date(etime);
                if (date.getTime() < date1.getTime()) {
                    tvTime.setText(TimeUtils.date2String(date, new SimpleDateFormat("YYYY-MM-dd HH:mm:ss")));
                } else {
                    ToastUtils.showShort("开始时间不能大于结束时间");
                    return;
                }
            }
        });

    }

    private void getEndStartTimePicker(final TextView tvTime) {
        final String stime = tvStartTime.getText().toString();
        TimePickerView pickerView = new TimePickerView(this, TimePickerView.Type.ALL);
        pickerView.show();
        pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                Date date1 = TimeUtils.string2Date(stime);
                if (date.getTime() > date1.getTime()) {
                    tvTime.setText(TimeUtils.date2String(date, new SimpleDateFormat("YYYY-MM-dd HH:mm:ss")));
                } else {
                    ToastUtils.showShort("结束时间不能小于开始时间");
                    return;
                }
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
        InputStream input = null;
        byte[] bytes = Base64.decode(path, Base64.DEFAULT);
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize = 8;
        //Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        input = new ByteArrayInputStream(bytes);
        SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(input, null, options));
        Bitmap bitmap = (Bitmap)softRef.get();
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        compositeDisposable.dispose();
    }
}
