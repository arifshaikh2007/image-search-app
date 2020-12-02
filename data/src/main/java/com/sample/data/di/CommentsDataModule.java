package com.sample.data.di;

import com.sample.data.db.AppDatabase;
import com.sample.data.db.CommentDao;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppDatabaseModule.class)
public class CommentsDataModule {
    @Provides
    public CommentDao provideCommentDao(AppDatabase appDatabase) {
        return appDatabase.commentDao();
    }
}
