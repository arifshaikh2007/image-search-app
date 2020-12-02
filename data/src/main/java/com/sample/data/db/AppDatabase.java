package com.sample.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.sample.domain.models.CommentModel;

/**
 * Room database class
 */
@Database(entities = {CommentModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CommentDao commentDao();
}

