package com.app.users.ui.listUser;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.app.users.R;
import com.app.users.data.local.UserModel;
import com.app.users.databinding.LayoutUserItemBinding;
import com.bumptech.glide.Glide;

import java.util.Locale;

public class UsersListAdapter extends ListAdapter<UserModel, UserViewHolder> {
    private final Context mContext;

    public UsersListAdapter(Context context, @NonNull DiffUtil.ItemCallback<UserModel> diffCallback) {
        super(diffCallback);
        this.mContext = context;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutUserItemBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_user_item, parent, false);
        mBinding.executePendingBindings();
        return UserViewHolder.create(mBinding);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserModel userModel = getItem(position);
        holder.bind(mContext, userModel);
    }

    static class UserDiff extends DiffUtil.ItemCallback<UserModel> {

        @Override
        public boolean areItemsTheSame(@NonNull UserModel oldItem, @NonNull UserModel newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull UserModel oldItem, @NonNull UserModel newItem) {
            return oldItem.getUid() == newItem.getUid();
        }
    }
}


class UserViewHolder extends RecyclerView.ViewHolder {
    private final LayoutUserItemBinding mBinding;

    public UserViewHolder(LayoutUserItemBinding itemView) {
        super(itemView.getRoot());
        mBinding = itemView;
    }

    public void bind(Context context, UserModel userModel) {
        mBinding.setUserModel(userModel);
        Glide.with(context).load(Uri.parse(userModel.profileImage)).into(mBinding.ivProfile);
        int imageId = context.getResources().getIdentifier(userModel.deviceType.toLowerCase(Locale.getDefault()), "drawable", context.getPackageName());
        Glide.with(context).load(imageId).into(mBinding.ivDeviceType);
    }

    static UserViewHolder create(LayoutUserItemBinding view) {
        return new UserViewHolder(view);
    }
}