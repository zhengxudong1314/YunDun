package com.dahua.searchandwarn.modules.facesearching.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
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
import com.dahua.searchandwarn.base.LoadingDialogUtils;
import com.dahua.searchandwarn.base.SW_Constracts;
import com.dahua.searchandwarn.model.SW_DeviceCodeBean;
import com.dahua.searchandwarn.model.SW_FaceCropBean;
import com.dahua.searchandwarn.model.SW_FaceParams;
import com.dahua.searchandwarn.modules.warning.activity.SW_TreeActivity;
import com.dahua.searchandwarn.net.SW_RestfulApi;
import com.dahua.searchandwarn.net.SW_RestfulClient;
import com.dahua.searchandwarn.utils.Base64FileUtils;
import com.dahua.searchandwarn.utils.LogUtils;
import com.dahua.searchandwarn.utils.TimeConstants;
import com.dahua.searchandwarn.utils.TimeUtils;
import com.dahua.searchandwarn.utils.ToastUtils;
import com.lvfq.pickerview.TimePickerView;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    private String endPath = new File("/facecrop/").getAbsolutePath();

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
                int min = 0;
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
        strStratTime = TimeUtils.date2String(date, new SimpleDateFormat("yyyy-MM-dd 00:00:00"));
        strEndTime = TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
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
            if (list != null) {
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
            if (TextUtils.isEmpty(etSimilarity.getText().toString())) {
                ToastUtils.showShort("请填写相似度");
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
            sw_faceParams.setOperator(SW_Constracts.getUserName(SW_FaceSearchingActivity.this));
            EventBus.getDefault().postSticky(sw_faceParams);
            startActivity(intent);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMood(SW_DeviceCodeBean bean) {
        if (TextUtils.isEmpty(bean.getAddress())) {
            tvSite.setText(R.string.choose);
        } else {
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

    //小米手机路径转换
    private String path = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    if (file != null) {
                        try {
                            String path = file.getAbsolutePath();
                            /*llFace.setVisibility(View.GONE);
                            ivFace.setVisibility(View.VISIBLE);
                            Bitmap bitmap = BitmapFactory.decodeFile(path);
                            FileOutputStream os = new FileOutputStream(endPath);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                            int bitmapDegree = getBitmapDegree(path);
                            Bitmap bitmap1 = rotateBitmapByDegree(bitmap, bitmapDegree);
                            ivFace.setImageBitmap(bitmap1);
                            //Glide.with(SW_FaceSearchingActivity.this).load(file).into(ivFace);
                            getPic(endPath);*/
                            Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                            Tiny.getInstance().source(path).asFile().withOptions(options).compress(new FileCallback() {
                                @Override
                                public void callback(boolean isSuccess, String outfile, Throwable t) {
                                    Bitmap bitmap = Base64FileUtils.fileToBitmap(outfile);
                                    int bitmapDegree = getBitmapDegree(outfile);
                                    Bitmap bitmap1 = rotateBitmapByDegree(bitmap, bitmapDegree);
                                    ivFace.setImageBitmap(bitmap1);
                                    //Glide.with(SW_FaceSearchingActivity.this).load(outfile).placeholder(R.drawable.sw_home_page_defect).into(ivFace);
                                    llFace.setVisibility(View.GONE);
                                    ivFace.setVisibility(View.VISIBLE);
                                    LogUtils.e(outfile);
                                    getPic(outfile);
                                }
                            });

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

                    if (data != null) {
                        Uri uri = data.getData();
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
                        }
                        /*InputStream inputStream = resolver.openInputStream(uri);

                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        int bitmapDegree = getBitmapDegree(path);
                        Bitmap bitmap1 = rotateBitmapByDegree(bitmap, bitmapDegree);
                        FileOutputStream os = new FileOutputStream(endPath);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        ivFace.setImageBitmap(bitmap1);
                        llFace.setVisibility(View.GONE);
                        ivFace.setVisibility(View.VISIBLE);
                        LogUtils.e(uri.toString());
                        getPic(endPath);
                        final String encode = Base64FileUtils.fileToBase64(path);
                        LogUtils.e(encode);*/


                        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                        Tiny.getInstance().source(path).asFile().withOptions(options).compress(new FileCallback() {
                            @Override
                            public void callback(boolean isSuccess, String outfile, Throwable t) {
                                Bitmap bitmap = Base64FileUtils.fileToBitmap(outfile);
                                int bitmapDegree = getBitmapDegree(outfile);
                                Bitmap bitmap1 = rotateBitmapByDegree(bitmap, bitmapDegree);
                                ivFace.setImageBitmap(bitmap1);
                                //Glide.with(SW_FaceSearchingActivity.this).load(outfile).placeholder(R.drawable.sw_home_page_defect).into(ivFace);
                                llFace.setVisibility(View.GONE);
                                ivFace.setVisibility(View.VISIBLE);
                                LogUtils.e(outfile);
                                getPic(path);
                                //return the compressed file path
                            }
                        });
                    }
                } catch (Exception e) {


                    e.printStackTrace();
                }
                break;
        }
    }

    private String encode = "";

    private void getPic(final String paths) {
        LoadingDialogUtils.show(SW_FaceSearchingActivity.this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    encode = Base64FileUtils.fileToBase64(paths);
                    //encode = Base64.encodeBase64String(getMulFileByPath(paths).getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final SW_RestfulApi restfulApi = SW_RestfulClient.getInstance().getRestfulApi(SW_Constracts.getBaseUrl(SW_FaceSearchingActivity.this));
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
                                                    Bitmap bitmap = Base64FileUtils.base64ToBitmap(item.getSmallImgBase64());
                                                    helper.setImageBitmap(R.id.iv_face, bitmap);
                                                    final CheckBox cb = helper.getView(R.id.cb);
                                                    final ImageView iv_img = helper.getView(R.id.iv_face);
                                                    iv_img.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            if (cb.isChecked()) {
                                                                cb.setChecked(false);
                                                            } else {
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
                                                                } else {
                                                                    for (int i = 0; i < list.size(); i++) {
                                                                        list.get(i).setChecked(false);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        } else {
                                            ToastUtils.showLong("获取人脸失败");
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        LoadingDialogUtils.dismiss();
                                        LogUtils.e("onError:" + e.getMessage());
                                        ToastUtils.showLong("获取人脸失败");
                                    }

                                    @Override
                                    public void onComplete() {
                                        LoadingDialogUtils.dismiss();
                                        LogUtils.e("onComplete");
                                    }
                                });
                    }
                });
            }
        }).start();

        // LogUtils.e(encode);

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
                    tvTime.setText(TimeUtils.date2String(date, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
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
                    tvTime.setText(TimeUtils.date2String(date, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
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



    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    private static MultipartFile getMulFileByPath(String picPath) {
        FileItem fileItem = createFileItem(picPath);
        MultipartFile mfile = new CommonsMultipartFile(fileItem);
        return mfile;
    }

    private static FileItem createFileItem(String filePath) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "textField";
        int num = filePath.lastIndexOf(".");
        String extFile = filePath.substring(num);
        FileItem item = factory.createItem(textFieldName, "text/plain", true,
                "MyFileName" + extFile);
        File newfile = new File(filePath);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(newfile);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192))
                    != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Tiny.getInstance().clearCompressDirectory();
        EventBus.getDefault().unregister(this);
        compositeDisposable.dispose();
    }
}
