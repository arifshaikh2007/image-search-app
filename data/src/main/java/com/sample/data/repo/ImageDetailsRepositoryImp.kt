package com.sample.data.repo

import com.sample.data.db.CommentDao
import com.sample.domain.models.CommentModel
import com.sample.domain.repositories.ImageDetailsRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ImageDetailsRepositoryImp @Inject constructor(private val commentDao: CommentDao) : ImageDetailsRepository {
    override fun getAllComments(imageId: String?): Single<MutableList<CommentModel>> {
        return commentDao.loadAllByImageId(imageId)
    }

    override fun saveComment(commentModel: CommentModel?): Completable {
        return commentDao.insertComment(commentModel)
    }
}