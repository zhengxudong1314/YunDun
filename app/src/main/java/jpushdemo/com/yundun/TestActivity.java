package jpushdemo.com.yundun;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dahua.searchandwarn.base.SW_Constracts;
import com.dahua.searchandwarn.model.SW_FaceCropBean;
import com.dahua.searchandwarn.model.SW_UserLoginBean;
import com.dahua.searchandwarn.net.SW_RestfulApi;
import com.dahua.searchandwarn.net.SW_RestfulClient;
import com.dahua.searchandwarn.utils.Base64FileUtils;
import com.dahua.searchandwarn.utils.LogUtils;
import com.dahua.searchandwarn.utils.Utils;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TestActivity extends AppCompatActivity {

    private ImageView iv1;
    private ImageView iv2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Utils.init(this.getApplication());
        Button xiangce = (Button) findViewById(R.id.xiangce);
        Button xiangji = (Button) findViewById(R.id.xiangji);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        xiangce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1001);
            }
        });
        xiangji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            ContentResolver resolver = getContentResolver();
            try {
                LogUtils.e(data.getData().toString());
                final String s = Base64FileUtils.fileToBase64(data.getData().toString());
                LogUtils.e(s);
                /*InputStream inputStream = resolver.openInputStream(data.getData());

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 40, baos); //参数如果为100那么就不压缩
                byte[] bytes = baos.toByteArray();
                final String strbm = Base64.encodeToString(bytes,Base64.DEFAULT);*/
                //get the image path
                /*String[] projection = {MediaStore.Images.Media.DATA};
                CursorLoader cursorLoader = new CursorLoader(this,data.getData(),projection,null,null,null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();

                String path = cursor.getString(column_index);
                final String s = Base64FileUtils.fileToBase64(path);*/
                final SW_RestfulApi restfulApi = SW_RestfulClient.getInstance().getRestfulApi(SW_Constracts.getBaseUrl(this));
                restfulApi.userLogin(SW_UserLoginBean.USERNANE, SW_UserLoginBean.PASSWORD)
                        .flatMap(new Function<SW_UserLoginBean, ObservableSource<SW_FaceCropBean>>() {
                            @Override
                            public ObservableSource<SW_FaceCropBean> apply(SW_UserLoginBean sw_userLoginBean) throws Exception {
                                if (sw_userLoginBean.getRetCode() == 0) {
                                    return restfulApi.getFaceCrop(null);
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

                            }

                            @Override
                            public void onNext(SW_FaceCropBean sw_faceCropBean) {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            } catch (Exception ex) {
            }

        }
    }


}
