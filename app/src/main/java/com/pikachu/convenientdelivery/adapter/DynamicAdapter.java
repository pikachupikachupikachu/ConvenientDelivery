package com.pikachu.convenientdelivery.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.model.DComment;
import com.pikachu.convenientdelivery.model.Dynamic;
import com.pikachu.convenientdelivery.model.User;
import com.pikachu.convenientdelivery.view.ClickShowMoreLayout;
import com.pikachu.convenientdelivery.view.CommentWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Howie Tian on 2017/7/28 0028.
 */

public class DynamicAdapter extends RecyclerView.Adapter<DynamicAdapter.DMyViewHolder> {
    private Context context;
    private List<Dynamic> dynamicList = new ArrayList<>();
    private Map<String, List<DComment>> commentMap = new HashMap<>();

    private onCommentClickListener mOnCommentListener;
    private onPraiseClickListener mOnPraiseClikcListener;

    public static final int REFRESH_PRAISE = 0;
    public static final int REFRESH_COMMENT = 1;
    public static final int REPLY_COMMENT = 2;

    private Handler handler;

    public DynamicAdapter(Context context, List<Dynamic> list, Map<String, List<DComment>> commentMap, Handler handler) {
        this.context = context;
        this.dynamicList = list;
        this.commentMap = commentMap;
        this.handler = handler;
    }

    //    点击评论的接口
    public interface onCommentClickListener {
        void onClick(int position);
    }

    public void setOnCommentListener(onCommentClickListener listener) {
        this.mOnCommentListener = listener;
    }


    //  点赞的接口
    public interface onPraiseClickListener {
        void onLikeClick(int position);

        void onUnLikeClick(int position);
    }

    public void setOnPraiseClickListener(onPraiseClickListener listener) {
        this.mOnPraiseClikcListener = listener;
    }

    @Override
    public DMyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_empty_content, parent, false);
        DMyViewHolder holder = new DMyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DMyViewHolder holder, int position) {
//            不用这个方法
    }

    @Override
    public void onBindViewHolder(DMyViewHolder holder, final int position, List<Object> payloads) {
        final Dynamic dynamic = dynamicList.get(position);
        List<DComment> commentList = commentMap.get(dynamic.getObjectId());
        if (commentList == null) {
            holder.commentAndPraiseLayout.setVisibility(View.GONE);
        } else {
            holder.commentAndPraiseLayout.setVisibility(View.VISIBLE);
        }
//        正常刷新
        if (payloads.isEmpty()) {
            holder.nick.setText(dynamic.getUser().getNick());
            if (dynamic.getUser().getAvatar() != null) {
                Glide.with(context).load(dynamic.getUser().getAvatar().getUrl()).into(holder.avatar);
            }
            holder.content.setText(dynamic.getContent());
            holder.time.setText(dynamic.getCreatedAt());
            if (dynamic.getLikeId() != null) {
                holder.tvLikeNum.setText(dynamic.getLikeId().size() + "");
            } else {
                holder.tvLikeNum.setText(0 + "");
            }
            if (commentList != null) {
                holder.commentLayout.setVisibility(View.VISIBLE);
                addCommentWidget(commentList, (DMyViewHolder) holder, position);
            }

            ArrayList<ImageInfo> imageInfoList = new ArrayList<>();
            if (dynamic.getImageUrls() != null) {
                for (String url : dynamic.getImageUrls()) {
                    ImageInfo info = new ImageInfo();
                    info.setThumbnailUrl(url);
                    info.setBigImageUrl(url);
                    imageInfoList.add(info);
                }
                Log.e("url", dynamic.getContent());
            }
            holder.nineGridView.setAdapter(new NineGridViewClickAdapter(context, imageInfoList));

            /**
             * 初始化时，判断点赞标志
             */
            if (dynamic.getLikeId() != null) {
                for (String id : dynamic.getLikeId()) {
                    if (id.equals(BmobUser.getCurrentUser(User.class).getObjectId())) {
                        holder.like.setLiked(true);
                    } else {
                        holder.like.setLiked(false);
                    }
                }
            }

            if (dynamic.getLikeId() == null) {
                holder.like.setLiked(false);
            }
        } else {
            int type = (int) payloads.get(0);
            switch (type) {
                case REFRESH_PRAISE:
                    holder.tvLikeNum.setText(dynamic.getLikeId().size() + "");
                    break;
                case REFRESH_COMMENT:
                    addCommentWidget(commentList, (DMyViewHolder) holder, position);
                    break;
            }
        }

        /**
         * 评论事件
         */
        if (mOnCommentListener != null) {
            holder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnCommentListener.onClick(position);
                }
            });
        }
        /**
         * 点赞事件
         */
        if (mOnPraiseClikcListener != null) {
            holder.like.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    mOnPraiseClikcListener.onLikeClick(position);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    mOnPraiseClikcListener.onUnLikeClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dynamicList.size();
    }

    public class DMyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatar;
        TextView nick;
        TextView time;
        ClickShowMoreLayout content;
        LinearLayout commentLayout;
        LinearLayout commentAndPraiseLayout;
        LikeButton like;
        LikeButton comment;
        TextView tvLikeNum;
        NineGridView nineGridView;

        public DMyViewHolder(View itemView) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            nick = (TextView) itemView.findViewById(R.id.nick);
            content = (ClickShowMoreLayout) itemView.findViewById(R.id.item_text_field);
            commentLayout = (LinearLayout) itemView.findViewById(R.id.comment_layout);
            like = (LikeButton) itemView.findViewById(R.id.like);
            comment = (LikeButton) itemView.findViewById(R.id.comment);
            tvLikeNum = (TextView) itemView.findViewById(R.id.tv_like_num);
            nineGridView = (NineGridView) itemView.findViewById(R.id.nineGridView);
            time = (TextView) itemView.findViewById(R.id.tv_create_time);
            commentAndPraiseLayout = (LinearLayout) itemView.findViewById(R.id.comment_praise_layout);
        }

    }

    private void addCommentWidget(final List<DComment> commentList, final DMyViewHolder holder, final int position) {
        if (commentList == null || commentList.size() == 0) {
            return;
        }
        /**
         * 优化方案
         * 滑动的时候必须要removeView或者addView
         * 但为了性能提高，不可以直接removeAllViews
         * 于是采取以下方案
         *      根据现有的view进行remove/add差额
         *      然后统一设置
         */
        final int childCount = holder.commentLayout.getChildCount();
        if (childCount < commentList.size()) {
//            若当前的view少于list的长度，则add相差的view
            int subCount = commentList.size() - childCount;
            for (int i = 0; i < subCount; i++) {
                CommentWidget commentWidget = new CommentWidget(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = 1;
                params.bottomMargin = 1;
                commentWidget.setLayoutParams(params);
                commentWidget.setLineSpacing(4, 1);

//                commentWidget.setOnLongClickListener(this);
                holder.commentLayout.addView(commentWidget);
            }
//            若当前的View 多于 list的长度，则remove相差的view
        } else if (childCount > commentList.size()) {
            holder.commentLayout.removeViews(commentList.size(), childCount - commentList.size());
        }
//        最终，不用重新removeall或者addAll 所有的CommentWidget,只要修修补补就可以了
//        然后把数据绑定好，即可
        for (int n = 0; n < commentList.size(); n++) {
            CommentWidget commentWidget = (CommentWidget) holder.commentLayout.getChildAt(n);
            final DComment dComment = commentList.get(n);
            if (commentWidget != null) {
                commentWidget.setCommentText(commentList.get(n));
                //                实现点击事件
                commentWidget.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                        Message message = handler.obtainMessage();
                        message.what = REPLY_COMMENT;
                        message.obj = dComment;
                        message.arg1 = position;
                        handler.sendMessage(message);

                    }
                });
            }

        }
    }
}
