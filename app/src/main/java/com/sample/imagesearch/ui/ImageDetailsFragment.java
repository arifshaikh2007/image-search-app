package com.sample.imagesearch.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sample.data.models.ImageDataModel;
import com.sample.domain.models.CommentModel;
import com.sample.imagesearch.R;
import com.sample.imagesearch.viewmodel.ImageDetailsViewModel;
import com.sample.imagesearch.viewmodel.SelectedImageDetailsViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class ImageDetailsFragment extends Fragment implements View.OnClickListener {
    private ImageDetailsViewModel mImageDetailsViewModel;
    private SelectedImageDetailsViewModel mSelectedImageDetailsViewModel;
    private CommentsListAdapter mCommentsListAdapter;

    private final Observer<ImageDataModel> observer = imageDataModel -> {
        final Bundle bundle = new Bundle();
        bundle.putParcelable("image_details", imageDataModel);
        if (getArguments() != null) {
            bundle.putAll(getArguments());
        }
        setArguments(bundle);
        setTitle(imageDataModel.getTitle());
        loadImageDetails(imageDataModel);
    };

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    public ImageDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View rootView = inflater.inflate(R.layout.fragment_image_details, container, false);
        rootView.findViewById(R.id.submit_comment_btn).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        AndroidSupportInjection.inject(this);
        mImageDetailsViewModel = viewModelFactory.create(ImageDetailsViewModel.class);
        mSelectedImageDetailsViewModel = new ViewModelProvider(requireActivity()).get(SelectedImageDetailsViewModel.class);//viewModelFactory.create(SelectedImageDetailsViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() == null ) {
            final View rootView = getView();
            Objects.requireNonNull(rootView).findViewById(R.id.selected_image).setVisibility(View.GONE);
            Objects.requireNonNull(rootView).findViewById(R.id.comments_section).setVisibility(View.GONE);
            Objects.requireNonNull(rootView).findViewById(R.id.comments_list).setVisibility(View.GONE);
            Objects.requireNonNull(rootView).findViewById(R.id.image_not_selected_text).setVisibility(View.VISIBLE);
        } else {
            loadImageDetails(getArguments().getParcelable("image_details"));
        }

        mSelectedImageDetailsViewModel.getImageDataModelLiveData().observe(requireActivity(), observer);
    }

    private void setTitle(String title) {
        if (getView() == null) {
            return;
        }

        final TextView textView = getView().findViewById(R.id.action_bar_text);
        if (textView != null) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(title);
        }
    }

    @Override
    public void onDestroyView() {
        mCommentsListAdapter = null;
        mSelectedImageDetailsViewModel.getImageDataModelLiveData().removeObserver(observer);
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_comment_btn) {
            final EditText editText = getView().findViewById(R.id.comment_edit_txt);
            final String enteredText = editText.getText().toString();
            if (!TextUtils.isEmpty(enteredText) && getArguments() != null) {
                final ImageDataModel imageDataModel = getArguments().getParcelable("image_details");
                mImageDetailsViewModel.saveComment(imageDataModel.getId(), enteredText);
                editText.setText("");
            } else {
                Toast.makeText(getContext(), R.string.enter_valid_comment, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadImageDetails(ImageDataModel imageDetailsModel) {
        View rootView = getView();
        if (rootView == null || imageDetailsModel == null) {
            return;
        }
        rootView.findViewById(R.id.selected_image).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.comments_section).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.comments_list).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.image_not_selected_text).setVisibility(View.GONE);

        Picasso.get().load(getMediumSizeImageUrl(imageDetailsModel.getUrl()))
                .fit()
                .centerCrop()
                .noFade()
                .placeholder(R.drawable.progress_animation)
                .into((ImageView) getView().findViewById(R.id.selected_image));

        mImageDetailsViewModel.mCommentsLiveData.observe(getViewLifecycleOwner(), comments -> handleCommentsList(comments));
        mImageDetailsViewModel.getAllCommentsByImageId(imageDetailsModel.getId());
    }

    private String getMediumSizeImageUrl(String imageUrl) {
        return new StringBuilder(imageUrl).insert(imageUrl.lastIndexOf("."), 'm').toString();
    }

    private void handleCommentsList(List<CommentModel> comments) {
        final View rootView = getView();
        if (rootView == null) {
            return;
        }
        RecyclerView recyclerView = rootView.findViewById(R.id.comments_list);
        if (comments.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            if (mCommentsListAdapter == null) {
                mCommentsListAdapter = new CommentsListAdapter(comments);
                recyclerView.setLayoutManager(new GridLayoutManager(rootView.getContext(), 1));
                recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
                recyclerView.setAdapter(mCommentsListAdapter);
            } else {
                mCommentsListAdapter.setCommentsList(comments);
            }
        }
    }
}

class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.CommentViewHolder> {
    private List<CommentModel> mComments;

    CommentsListAdapter(List<CommentModel> comments) {
        mComments = comments;
    }

    public void setCommentsList(List<CommentModel> comments) {
        mComments = comments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comment, parent, false);
        return new CommentViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.bindComment(mComments.get(position));
    }

    @Override
    public int getItemCount() {
        return mComments != null ? mComments.size() : 0;
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.comment_txt);
        }

        void bindComment(CommentModel commentModel) {
            textView.setText(commentModel.comment);
        }
    }
}
