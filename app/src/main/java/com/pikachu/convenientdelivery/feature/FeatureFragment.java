package com.pikachu.convenientdelivery.feature;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.melnykov.fab.FloatingActionButton;
import com.pikachu.convenientdelivery.CityPickerActivity;
import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.adapter.DynamicAdapter;
import com.pikachu.convenientdelivery.application.Constant;
import com.pikachu.convenientdelivery.base.BaseFragment;
import com.pikachu.convenientdelivery.databinding.FragmentFeatureBinding;
import com.pikachu.convenientdelivery.databinding.ToolbarPickCityBinding;
import com.pikachu.convenientdelivery.db.DBManager;
import com.pikachu.convenientdelivery.model.DComment;
import com.pikachu.convenientdelivery.model.Dynamic;
import com.pikachu.convenientdelivery.model.User;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static android.content.Context.SEARCH_SERVICE;

/**
 * 精选
 */

public class FeatureFragment extends BaseFragment<FragmentFeatureBinding> implements View.OnClickListener, SearchView.OnQueryTextListener {

    public static final String ARGUMENT = "argument";

    private SmartRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private ToolbarPickCityBinding toolbarPickCityBinding;
    private LinearLayout toolbarPickCity;
    private Banner banner;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private FloatingActionButton fab;
    private AppBarLayout appBarLayout;
    private NestedScrollView scrollView;

    private List<Dynamic> dynamicList = new ArrayList<>();
    // 单个item点赞ID集合，用于初始化布局界面
    private ArrayList<String> likeIdList = new ArrayList<>();
    //    查询到commentMap
    private Map<String, List<DComment>> commentMap = new HashMap<>();
    private DynamicAdapter dynamicAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private int limit = Constant.LIMIT;
    private String lastTime;
    private static final int PULL_REFRESH = 0;
    private static final int LOAD_MORE = 1;
    private List<String> urlList = new ArrayList<>();


    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DynamicAdapter.REPLY_COMMENT) {

                DComment comment = (DComment) msg.obj;
                int position = msg.arg1;
                showReplyPopup(comment.getDynamicId(), position, comment.getAuthor());
                popupInputMethodWindow();

            }
        }
    };

    public static FeatureFragment newInstance(String argument) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, argument);
        FeatureFragment featureFragment = new FeatureFragment();
        featureFragment.setArguments(bundle);
        return featureFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_feature;
    }

    @Override
    protected void init() {
        super.init();
        initView();
        initDatas();
        initListener();
    }

    private void initDatas() {
        swipeRefreshLayout.autoRefresh();
        urlList.clear();
        urlList.add("http://image.qjwb.com.cn/group1/M00/01/1B/CggkA1fDfVSAbV7jABxuw-Jc4KE779.jpg");
        urlList.add("http://www.getdigsy.com/blog/wp-content/uploads/2016/12/industrial-hall-1630740_1280.jpg");
        urlList.add("https://www.leadbook.com/wp-content/uploads/2017/02/our-data1.jpeg");
        banner.setImages(urlList).setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        }).start();

        dynamicAdapter = new DynamicAdapter(getContext(), dynamicList, commentMap, mhandler);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(dynamicAdapter);
        recyclerView.setHasFixedSize(true);
//        解决嵌套时滑动黏滞
        recyclerView.setNestedScrollingEnabled(false);

    }

    private void initView() {
        swipeRefreshLayout = bindingView.swipeRefreshLayout;
        toolbar = bindingView.toolbar;
        recyclerView = bindingView.recyclerView;
        fab = bindingView.fabPub;
        appBarLayout = bindingView.appbar;
        banner = bindingView.banner;
        toolbarPickCityBinding = bindingView.toolbarPickCity;
        scrollView = bindingView.nestedScrollViewCircle;

        toolbar.inflateMenu(R.menu.toolbar_search);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);


    }

    private void initListener() {
        toolbarPickCityBinding.pickCity.setOnClickListener(this);
        fab.setOnClickListener(this);
        loadMore();
        refresh();
        showOrHideFab();
        comment();
        praise();
    }

    private void refresh() {
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData(PULL_REFRESH);
            }
        });
    }

    private void loadMore() {

        swipeRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getData(LOAD_MORE);
            }
        });
    }

    private void showOrHideFab() {
        // 通过监听AppbarLayout的位置，来控制fab的显示与隐藏
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean misAppbarExpand = true;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scrollRange = appBarLayout.getTotalScrollRange();
                float fraction = 1f * (scrollRange + verticalOffset) / scrollRange;
                if (fraction < 0.1 && misAppbarExpand) {
                    misAppbarExpand = false;
                    fab.hide();
                }
                if (fraction > 0.8 && !misAppbarExpand) {
                    misAppbarExpand = true;
                    fab.show();
                }
            }
        });
    }

    /**
     * 评论功能
     */

    public void comment() {
        dynamicAdapter.setOnCommentListener(new DynamicAdapter.onCommentClickListener() {
            @Override
            public void onClick(int position) {
                showPopup(dynamicList.get(position), position);
                popupInputMethodWindow();
            }
        });
    }

    /**
     * 点赞事件
     */

    private void praise() {
        dynamicAdapter.setOnPraiseClickListener(new DynamicAdapter.onPraiseClickListener() {
            @Override
            public void onLikeClick(final int position) {

                final User currentUser = BmobUser.getCurrentUser(User.class);
                final Dynamic dynamic = dynamicList.get(position);
                if (likeIdList != null) {
                    likeIdList.clear();
                }
                if (dynamic.getLikeId() != null) {
                    likeIdList = dynamic.getLikeId();
                }
                likeIdList.add(currentUser.getObjectId());
                dynamic.setLikeId(likeIdList);
                dynamic.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            dynamicAdapter.notifyItemChanged(position, DynamicAdapter.REFRESH_PRAISE);
                        } else {
                            showToast("点赞更新失败" + e.getMessage() + e.getErrorCode());
                        }
                    }
                });

            }

            @Override
            public void onUnLikeClick(final int position) {

                final User currentUser = BmobUser.getCurrentUser(User.class);
                final Dynamic dynamic = dynamicList.get(position);
                if (likeIdList != null) {
                    likeIdList.clear();
                }

                if (dynamic.getLikeId() != null) {
                    likeIdList = dynamic.getLikeId();
                }
                likeIdList.remove(currentUser.getObjectId());

                dynamic.setLikeId(likeIdList);
                dynamic.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            dynamicAdapter.notifyItemChanged(position, DynamicAdapter.REFRESH_PRAISE);
                        } else {
                            showToast("取消点赞更新失败" + e.getMessage() + e.getErrorCode());
                        }
                    }
                });

            }
        });
    }

    private void getData(final int type) {

        final BmobQuery<Dynamic> query = new BmobQuery<>();
        query.include("user");
        query.order("-createdAt");
//        设置每页查询的item数目
        query.setLimit(limit);

//        加载更多
        if (type == LOAD_MORE) {
//         处理时间查询
            Date date = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = dateFormat.parse(lastTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
//         只查询小于等于最后一个item发表时间的数据
            query.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
        }

        query.findObjects(new FindListener<Dynamic>() {
            @Override
            public void done(List<Dynamic> list, BmobException e) {
                if (e == null) {
//                    查出有数据
                    if (list.size() > 0) {
                        if (type == PULL_REFRESH) {
                            dynamicList.clear();
                        }
                        List<String> dynamicIds = new ArrayList<String>();
                        for (int i = 0; i < list.size(); i++) {
                            dynamicIds.add(list.get(i).getObjectId());
                        }
//   **************************************************************************************
                        BmobQuery<DComment> commentQuery = new BmobQuery<DComment>();
                        commentQuery.addWhereContainedIn("dynamicId", dynamicIds);
                        commentQuery.include("author,reply");
                        commentQuery.findObjects(new FindListener<DComment>() {
                            @Override
                            public void done(List<DComment> list, BmobException e) {
                                if (e == null) {
                                    if (type == PULL_REFRESH) {
                                        commentMap.clear();
                                    }
                                    //    查询到commentMap
//                                        分组算法
                                    for (DComment comment : list) {
                                        List<DComment> commentList = commentMap.get(comment.getDynamicId());
                                        if (commentList == null) {
                                            commentList = new ArrayList<DComment>();
                                            commentList.add(comment);
                                            commentMap.put(comment.getDynamicId(), commentList);
                                        } else {
                                            commentList.add(comment);
                                        }
                                    }
                                    dynamicAdapter.notifyDataSetChanged();

                                } else {
                                    showToast("查询评论失败" + e.getMessage() + e.getErrorCode());
                                }
                            }
                        });
//           ****************************************************************************************************
                        dynamicList.addAll(list);
                        dynamicAdapter.notifyDataSetChanged();
                        lastTime = dynamicList.get(dynamicList.size() - 1).getCreatedAt();
                        Log.e("HHH", lastTime);
//                        查询到无数据
                        if (type == PULL_REFRESH) {
                            swipeRefreshLayout.finishRefresh();
                        } else {
                            swipeRefreshLayout.finishLoadmore();
                        }
                    } else {
                        if (type == LOAD_MORE) {
                            showToast("没有更多数据了");
                            swipeRefreshLayout.finishLoadmore();
                        } else if (type == PULL_REFRESH) {
                            showToast("服务器没有数据");
                            swipeRefreshLayout.finishRefresh();
                        }
                    }
                } else {
                    showToast("请求服务器异常" + e.getMessage() + "错误代码" + e.getErrorCode());
                    Log.e("HHH", e.getMessage() + "错误代码" + e.getErrorCode());
                    swipeRefreshLayout.finishRefresh();
                }
            }
        });
    }

    // 原创评论的弹出窗口
    private void showPopup(final Dynamic dynamic, final int position) {

        View parent = LayoutInflater.from(getContext()).inflate(R.layout.fragment_feature, null);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.comment_view, null);

        final PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        final EditText etComment = (EditText) view.findViewById(R.id.et_comment_text);
        Button btnSend = (Button) view.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = etComment.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    showToast("评论内容不能为空哦");
                    return;
                }
                final DComment dcomment = new DComment();
                dcomment.setContent(content);
                dcomment.setAuthor(BmobUser.getCurrentUser(User.class));
                dcomment.setDynamic(dynamic);
                dcomment.setDynamicId(dynamic.getObjectId());

                dcomment.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
//                    发送成功后，对话框直接消失
                            popupWindow.dismiss();
                            List<DComment> commentList = commentMap.get(dynamic.getObjectId());
                            if (commentList == null) {
                                commentList = new ArrayList<DComment>();
                                commentList.add(dcomment);
                                commentMap.put(dynamic.getObjectId(), commentList);
                            } else {
                                commentList.add(dcomment);
                            }

                            Log.e("HHHH", commentList.toString());
                            dynamicAdapter.notifyItemChanged(position, DynamicAdapter.REFRESH_COMMENT);


                        } else {
                            showToast("评论上传失败" + e.getMessage() + e.getErrorCode());
                        }
                    }
                });
            }
        });

//      弹出动画
        //   popupWindow.setAnimationStyle();
//      使其聚焦，要想监听菜单控件的时间就必须要调用此方法
        popupWindow.setFocusable(true);
//      设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
//      设置背景，这个是为了点击返回 back 也能使其消失，并且不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//       软键盘不会挡着popupwindow
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        设置菜单显示的位置
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
//        监听菜单的关闭事件
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

//                在popupwindow结束后，重新查询评论列表
                //queryCommentList();
            }
        });
//        监听触屏事件
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
    }

    //    评论回复的弹出窗口
    private void showReplyPopup(final String dynamicId, final int position, final User replyUser) {
        View parent = LayoutInflater.from(getContext()).inflate(R.layout.fragment_feature, null);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.comment_view, null);

        final PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        final EditText etComment = (EditText) view.findViewById(R.id.et_comment_text);
        etComment.setHint("回复"+replyUser.getNick()+":");
        Button btnSend = (Button) view.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = etComment.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    showToast("评论内容不能为空哦");
                    return;
                }
                final DComment dcomment = new DComment();
                dcomment.setContent(content);
                dcomment.setAuthor(BmobUser.getCurrentUser(User.class));
                dcomment.setReply(replyUser);
                dcomment.setDynamic(null);
                dcomment.setDynamicId(dynamicId);

                dcomment.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
//                    发送成功后，对话框直接消失
                            popupWindow.dismiss();
                            List<DComment> commentList = commentMap.get(dynamicId);
                            if (commentList == null) {
                                commentList = new ArrayList<DComment>();
                                commentList.add(dcomment);
                                commentMap.put(dynamicId, commentList);
                            } else {
                                commentList.add(dcomment);
                            }
                            dynamicAdapter.notifyItemChanged(position, DynamicAdapter.REFRESH_COMMENT);
                        } else {
                            showToast("评论上传失败" + e.getMessage() + e.getErrorCode());
                        }
                    }
                });
            }
        });

//      弹出动画
        //   popupWindow.setAnimationStyle();
//      使其聚焦，要想监听菜单控件的时间就必须要调用此方法
        popupWindow.setFocusable(true);
//      设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
//      设置背景，这个是为了点击返回 back 也能使其消失，并且不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//       软键盘不会挡着popupwindow
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        设置菜单显示的位置
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
//        监听菜单的关闭事件
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

//                在popupwindow结束后，重新查询评论列表
                //queryCommentList();
            }
        });
//        监听触屏事件
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
    }

    //    弹出软键盘的方法
    private void popupInputMethodWindow() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.toolbar_search, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(toolbar.getMenu().findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                String city = DBManager.getPickedCity();
                toolbarPickCityBinding.pickedCity.setText(city);
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_pick_city:
                Intent intent = new Intent(getActivity(), CityPickerActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.fab_pub:
                jumpTo(PubDynamicActivity.class, false);
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
