package com.sample.domain.repositories;

import com.sample.domain.models.CommentModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface ImageDetailsRepository {
    Completable saveComment(CommentModel commentModel);
    Single<List<CommentModel>> getAllComments(String imageId);
}
