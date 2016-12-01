package com.hana053.micropost.ui.pages.relateduserlist;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.hana053.micropost.databinding.UserItemBinding;
import com.hana053.micropost.domain.RelatedUser;
import com.hana053.micropost.services.AuthTokenService;
import com.hana053.micropost.ui.components.avatar.AvatarViewListener;
import com.hana053.micropost.ui.components.avatar.AvatarViewModel;
import com.hana053.micropost.ui.components.followbtn.FollowBtnViewListener;
import com.hana053.micropost.ui.components.followbtn.FollowBtnViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RelatedUserListAdapter extends RecyclerView.Adapter<RelatedUserListAdapter.ViewHolder> {

    private final List<RelatedUser> users = new ArrayList<>();
    private final AuthTokenService authTokenService;

    public RelatedUserListAdapter(AuthTokenService authTokenService) {
        this.authTokenService = authTokenService;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RelatedUser relatedUser = users.get(position);
        final boolean isMyself = authTokenService.isMyself(relatedUser);

        final RelatedUserItemViewModel viewModel = new RelatedUserItemViewModel(relatedUser, isMyself);
        holder.binding.setModel(viewModel);

        final FollowBtnViewModel followBtnViewModel = new FollowBtnViewModel(relatedUser.getId(), relatedUser.isFollowedByMe);
        holder.binding.setFollowBtnModel(followBtnViewModel);
        holder.binding.setFollowBtnListener(new FollowBtnViewListener(followBtnViewModel));

        final AvatarViewModel avatarModel = new AvatarViewModel(relatedUser);
        holder.binding.setAvatarModel(avatarModel);
        holder.binding.setAvatarListener(new AvatarViewListener(avatarModel));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    List<RelatedUser> getItems() {
        return new ArrayList<>(users);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final UserItemBinding binding;

        ViewHolder(UserItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private static ViewHolder newInstance(ViewGroup parent) {
            final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            final UserItemBinding binding = UserItemBinding.inflate(inflater, parent, false);
            return new ViewHolder(binding);
        }
    }

    @Nullable
    Long getLastItemId() {
        return Stream.of(users)
                .sortBy(u -> -users.indexOf(u))
                .findFirst()
                .map(RelatedUser::getRelationshipId)
                .orElse(null);
    }

    public boolean addAll(int location, Collection<RelatedUser> users) {
        if (this.users.addAll(location, users)) {
            notifyItemRangeInserted(location, users.size());
            return true;
        }
        return false;
    }

}