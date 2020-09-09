package com.mvp.activity.main;

import android.app.Activity;

import androidx.fragment.app.FragmentManager;

import com.mvp.api.ApiRetrofit;
import com.mvp.api.ApiServer;
import com.mvp.base.BaseContent;
import com.mvp.base.file.FileObserver;
import com.mvp.base.file.ProgressRequestBody;
import com.mvp.base.mvp.BaseModel;
import com.mvp.base.mvp.BaseObserver;
import com.mvp.base.mvp.BasePresenter;
import com.mvp.bean.MainBean;
import com.mvp.bean.MainBean2;
import com.mvp.bean.TextBean;
import com.mvp.commonutils.RetrofitUtil;
import com.mvp.view.dialog.CommonDialogFragment;
import com.mvp.view.dialog.DialogFragmentHelper;
import com.mvp.view.dialog.IDialogResultListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 *
 */
public class RxPresenter extends BasePresenter<RxView> {
    //private RxView rxView;
    private Activity mActivity;
    private FragmentManager mFragmentManager;

    public RxPresenter(RxView baseView, Activity activity, FragmentManager fragmentManager) {
        super(baseView);
        // rxView = baseView;
        mActivity = activity;
        mFragmentManager = fragmentManager;
    }

    ArrayList list = new ArrayList();

    public void initBtn() {
        list.clear();
        list.add(new RxAdapter.ItemModel("网络请求", 0,false));
        list.add(new RxAdapter.ItemModel("上传图片", 1,false));
        list.add(new RxAdapter.ItemModel("上传文件进度演示", 2,false));
        list.add(new RxAdapter.ItemModel("MVC请求演示", 3,false));
        list.add(new RxAdapter.ItemModel("测试多baseUrl1", 4,false));
        list.add(new RxAdapter.ItemModel("测试多baseUrl2", 5,false));
        list.add(new RxAdapter.ItemModel("测试多baseUrl3", 6,false));
        list.add(new RxAdapter.ItemModel("下载文件演示", 7,false));
        list.add(new RxAdapter.ItemModel("其他查询", 8,false));
        baseView.initBtn(list);

    }

    /**
     * 客户标签的筛选项
     */
    public void getTableListApi() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", "032cc080947549c83c3296026b5963a2");
        ApiServer apiServer = ApiRetrofit.getBaseUrlInstance("http://selfec.qhzx.online/").getApiService();
        addDisposable(apiServer.getTableList(params), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                baseView.onTableListSuccess((BaseModel<Object>) o);
            }

            @Override
            public void onError(String msg) {
                if (baseView != null) {
                    baseView.showError(msg);
                }
            }
        });
    }

    /**
     * 当前城市限行
     */
    public void getRestrictionsApi() {
        HashMap<String, String> params = new HashMap<>();
        params.put("city_code", "131");
        params.put("day", "20190802");
        ApiServer apiServer = ApiRetrofit.getBaseUrlInstance("http://www.qichexiaobaomu.com/").getApiService();
        addDisposable(apiServer.getRestrictions(params), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                baseView.onRestrictionsSuccess((BaseModel<Object>) o);
            }

            @Override
            public void onError(String msg) {
                if (baseView != null) {
                    baseView.showError(msg);
                }
            }
        });
    }

    /**
     * 测试
     */
    public void getCheShiApi() {
        HashMap<String, String> params = new HashMap<>();
        params.put("type", "1");
        ApiServer apiServer = ApiRetrofit.getBaseUrlInstance("http://www.energy-link.com.cn/").getApiService();
        addDisposable(apiServer.getCeShi(params), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                baseView.onCheShiSuccess((BaseModel<Object>) o);
            }

            @Override
            public void onError(String msg) {
                if (baseView != null) {
                    baseView.showError(msg);
                }
            }
        });
    }


    /**
     * 测试
     */
    public void getCheShiApi2() {
        HashMap<String, String> params = new HashMap<>();
        params.put("equipmentCode", "201904190003000");
        ApiServer apiServer = ApiRetrofit.getBaseUrlInstance("http://www.nj-lsj.net/Taste/").getApiService();
        addDisposable(apiServer.getCeShi2(params), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                baseView.onCheShiSuccess((BaseModel<Object>) o);

            }

            @Override
            public void onError(String msg) {
                if (baseView != null) {
                    baseView.showError(msg);
                }
            }
        });
    }

    /**
     * 写法好多种  怎么顺手怎么来
     */
    public void getManApi() {
        addDisposable(apiServer.getMain("year"), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                baseView.onMainSuccess((BaseModel<List<MainBean>>) o);
            }

            @Override
            public void onError(String msg) {
                if (baseView != null) {
                    baseView.showError(msg);
                }
            }
        });
    }

    /**
     * 写法好多种  怎么顺手怎么来
     */
    public void getMan2Api() {
        addDisposable(apiServer.getMain2("year"), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                baseView.onMainSuccess((BaseModel<List<MainBean>>) o);
            }

            @Override
            public void onError(String msg) {
                if (baseView != null) {
                    baseView.showError(msg);
                }
            }
        });
    }

    /**
     * 写法好多种  怎么顺手怎么来
     */
    public void getMan3Api() {
        HashMap<String, String> params = new HashMap<>();
        params.put("time", "year");
        addDisposable(apiServer.getMain3(params), new BaseObserver(baseView) {

            @Override
            public void onSuccess(BaseModel o) {
                baseView.onMainSuccess((BaseModel<List<MainBean>>) o);
            }

            @Override
            public void onError(String msg) {
                if (baseView != null) {
                    baseView.showError(msg);
                }
            }
        });
    }

    /**
     * 写法好多种  怎么顺手怎么来
     */
    public void getTextApi() {
        HashMap<String, String> params = new HashMap<>();
        params.put("type", "junshi");
        params.put("key", "2c1cb93f8c7430a754bc3ad62e0fac06");
        addDisposable(apiServer.getText(params), new BaseObserver(baseView) {

            @Override
            public void onSuccess(BaseModel o) {
                baseView.onTextSuccess((BaseModel<TextBean>) o);
            }

            @Override
            public void onError(String msg) {
                if (baseView != null) {
                    baseView.showError(msg);
                }
            }
        });
    }


    /*>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  图片上传  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/

    /**
     * 演示单图上传
     *
     * @param parts
     */
    public void upLoadImgApi(MultipartBody.Part parts) {
        addDisposable(apiServer.upLoadImg(parts), new BaseObserver(baseView) {

            @Override
            public void onSuccess(BaseModel o) {
                baseView.onUpLoadImgSuccess((BaseModel<Object>) o);
            }

            @Override
            public void onError(String msg) {
                if (baseView != null) {
                    baseView.showError(msg);
                }
            }
        });
    }


    /**
     * 演示多图上传
     *
     * @param parts
     */
    public void upLoadImgApi(List<MultipartBody.Part> parts) {
        addDisposable(apiServer.upHeadImg(parts), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                baseView.onUpLoadImgSuccess((BaseModel<Object>) o);
            }

            @Override
            public void onError(String msg) {
                if (baseView != null) {
                    baseView.showError(msg);
                }
            }
        });
    }

    /**
     * 演示 图片和字段一起上传
     *
     * @param title
     * @param content
     * @param parts
     */
    public void upLoadImgApi(String title, String content, List<MultipartBody.Part> parts) {
        HashMap<String, RequestBody> params = new HashMap<>();
        params.put("title", RetrofitUtil.convertToRequestBody(title));
        params.put("content", RetrofitUtil.convertToRequestBody(content));
        addDisposable(apiServer.expressAdd(params, parts), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                baseView.onUpLoadImgSuccess((BaseModel<Object>) o);
            }

            @Override
            public void onError(String msg) {
                if (baseView != null) {
                    baseView.showError(msg);
                }
            }
        });
    }

    /**
     * 演示 文件上传进度监听
     *
     * @param url
     */
    public void upLoadVideoApi(String url) {
        HashMap<String, RequestBody> params = new HashMap<>();
        params.put("fileType", RetrofitUtil.convertToRequestBody("video"));

        MultipartBody.Part parts = MultipartBody.Part.createFormData("file", new File(url).getName(), new ProgressRequestBody(new File(url), "video/mpeg", baseView));

        ApiServer apiServer = ApiRetrofit.getBaseUrlInstance("https://bjlzbt.com/").getApiService();
        addFileDisposable(apiServer.getUpload(params, parts), new FileObserver(baseView) {
            @Override
            public void onSuccess(Object o) {
                baseView.onUpLoadImgSuccess((BaseModel<Object>) o);
            }

            @Override
            public void onError(String msg) {
                if (baseView != null) {
                    baseView.showError(msg);
                }
            }
        });
    }

    /**
     * 单个文件上传
     *
     * @param parts 文件流
     * @return
     */
    public void getUploadApi(HashMap<String, RequestBody> params, MultipartBody.Part parts) {
        ApiServer apiServer = ApiRetrofit.getBaseUrlInstance("https://bjlzbt.com/").getApiService();
        addDisposable(apiServer.getUpload(params, parts), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {

                baseView.onUpLoadImgSuccess((BaseModel<Object>) o);
            }

            @Override
            public void onError(String msg) {
                if (baseView != null) {
                    baseView.showError(msg);
                }
            }
        });
    }


    /**
     * myother
     *
     * @return
     */
    public void myother() {


        HashMap<String, String> params = new HashMap<>();
        params.put("equipmentCode", "201904190003000");


//        ApiServer apiServer = ApiRetrofit.getBaseUrlInstance("http://www.nj-lsj.net/Taste/").getApiService();
//        addDisposable(apiServer.myother(params), new BaseObserver(baseView) {
//            @Override
//            public void onSuccess(BaseModel o) {//更改basemOdEl
//                o.getResult();
//                Log.i("-----", "onSuccess: "+  o.getResult());
//                baseView.onMyOtherSuccess(o);//此处的数据格式是错误的信息会报错，小公司这些格式不统一
//                                                //很多正常开发当中都是自定随意定义的格式，所以建议使用的方式为下面的方式
//            }
//
//            @Override
//            public void onError(String msg) {
//                if (baseView != null) {
//                    baseView.showError(msg);
//                }
//            }
//        });


        ApiRetrofit.getBaseUrlInstance("https://www.nj-lsj.net/Taste/").getApiService()
                .myother(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new io.reactivex.functions.Function<MainBean2, MainBean2>() {
                    @Override
                    public MainBean2 apply(MainBean2 objectBaseModel) throws Exception {
                        return objectBaseModel;
                    }
                }).subscribe(new Consumer<MainBean2>() {
            @Override
            public void accept(MainBean2 o) throws Exception {
                //mTvText.setText(o.getResult().getData().toString());
                //此处使用handler 更新前端UI
                //LogUtils.i("-----", "onSuccess: "+  o.toString());
                // baseView.onUpLoadImgSuccess((BaseModel<Object>) o);

                baseView.onMyOtherSuccess(o);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }

    public void getDataApi2() {
        baseView.getDataApi2();
    }

    public void getDataApi() {
        baseView.getDataApi();
    }

    public void ListItemOnClick(int position) {
        switch (position) {
            case 0:
                //获取对话框需要挪到view 当中处理
                DialogFragmentHelper.showConfirmDialog(mFragmentManager, "ss", new IDialogResultListener<Integer>() {
                    @Override
                    public void onDataResult(Integer result) {

                    }
                }, true, new CommonDialogFragment.OnDialogCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                });
                getTextApi();
                break;
            case 1:
                getDataApi2();
//                try {
//                    //mPresenter.getDataApi2();
//
//                    Method getDataApi2 =  ReflectionUtils.getMethod( Class.forName("com.mvp.activity.main.RxActivity"),"getDataApi2",null);
//                    ReflectionUtils.invokeMethod(getDataApi2,new RxActivity(),null);
//
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                }

                break;
            case 2:
                upLoadVideoApi(BaseContent.baseFileName + "ceshi.mp4");

                break;
            case 3:
                getCheShiApi();
                break;
            case 4:
                getTableListApi();
                break;
            case 5:
                getRestrictionsApi();
                break;
            case 6:
                getCheShiApi2();
                break;
            case 7:
                getDataApi();
                //方法
//                try {
//                    //mPresenter.getDataApi2();
//                    Method getDataApi2 =  ReflectionUtils.getMethod( Class.forName("com.mvp.activity.main.RxActivity"),"getDataApi",null);
//                    ReflectionUtils.invokeMethod(getDataApi2,new RxActivity(),null);
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                }

                break;
            case 8:
                myother();
                break;
            case 9:
                break;

        }
    }
}
