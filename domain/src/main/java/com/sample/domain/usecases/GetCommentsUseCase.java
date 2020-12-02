package com.sample.domain.usecases;

import com.sample.domain.models.CommentModel;
import com.sample.domain.repositories.ImageDetailsRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class GetCommentsUseCase implements SingleUseCase<List<CommentModel>> {
    private final ImageDetailsRepository mImageDetailsRepository;

    @Inject
    public GetCommentsUseCase(ImageDetailsRepository imageDetailsRepository) {
        mImageDetailsRepository = imageDetailsRepository;
    }

    @NotNull
    @Override
    public Single<List<CommentModel>> execute(@NotNull String imageId) {
        return mImageDetailsRepository.getAllComments(imageId);
    }
}