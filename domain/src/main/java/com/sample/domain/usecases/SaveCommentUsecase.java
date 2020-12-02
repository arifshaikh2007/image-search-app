package com.sample.domain.usecases;

import com.sample.domain.models.CommentModel;
import com.sample.domain.repositories.ImageDetailsRepository;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import io.reactivex.Completable;

public class SaveCommentUsecase implements CompletableUsecase<CommentModel> {
    private final ImageDetailsRepository mImageDetailsRepository;

    @Inject
    public SaveCommentUsecase(ImageDetailsRepository imageDetailsRepository) {
        mImageDetailsRepository = imageDetailsRepository;
    }

    @NotNull
    @Override
    public Completable execute(@NotNull CommentModel commentDetails) {
       return mImageDetailsRepository.saveComment(commentDetails);
    }
}
