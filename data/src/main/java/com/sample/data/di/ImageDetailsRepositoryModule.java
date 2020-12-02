package com.sample.data.di;

import com.sample.data.db.CommentDao;
import com.sample.data.repo.ImageDetailsRepositoryImp;
import com.sample.domain.repositories.ImageDetailsRepository;

import dagger.Module;
import dagger.Provides;

@Module(includes = CommentsDataModule.class)
public class ImageDetailsRepositoryModule {
    @Provides
    public ImageDetailsRepository provideImageDetailsRepository(CommentDao commentDao) {
        return new ImageDetailsRepositoryImp(commentDao);
    }
}
