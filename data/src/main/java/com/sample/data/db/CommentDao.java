package com.sample.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sample.domain.models.CommentModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Dao class to get comments and to save comment into room db
 */
@Dao
public interface CommentDao {
    @Query("SELECT * FROM comments WHERE image_id IN (:imageId) order by id desc")
    Single<List<CommentModel>> loadAllByImageId(String imageId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertComment(CommentModel comment);
}
