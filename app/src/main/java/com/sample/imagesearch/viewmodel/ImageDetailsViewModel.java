package com.sample.imagesearch.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sample.data.db.CommentDao;
import com.sample.domain.models.CommentModel;
import com.sample.domain.usecases.GetCommentsUseCase;
import com.sample.domain.usecases.SaveCommentUsecase;
import com.sample.imagesearch.rx.SchedulersProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * This class is view model class for images details fragment.
 * Provides apis to save comment entered by user to database
 * and retrieve all comments for given image id.
 */
public class ImageDetailsViewModel extends ViewModel {
    private static final String TAG = "ImageDetailsVModel";

    private final SaveCommentUsecase mSaveCommentUsecase;
    private final GetCommentsUseCase mGetCommentsUseCase;
    private final SchedulersProvider mSchedulers;

    @Inject
    CommentDao commentDao;

    public final MutableLiveData<List<CommentModel>> mCommentsLiveData = new MutableLiveData<>();

    @Inject
    public ImageDetailsViewModel(SaveCommentUsecase saveCommentUsecase,
                                 GetCommentsUseCase getCommentsUseCase,
                                 SchedulersProvider schedulers) {
        this.mSaveCommentUsecase = saveCommentUsecase;
        this.mGetCommentsUseCase = getCommentsUseCase;
        this.mSchedulers = schedulers;
    }

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    /**
     * This method saves passed comment for given image id in db
     * @param imageId id of image for which comment needs to be stored
     * @param comment comment text to store for given image id
     */
    public void saveComment(String imageId, String comment) {
        final CommentModel commentModel = new CommentModel();
        commentModel.imageId = imageId;
        commentModel.comment = comment;
        mSaveCommentUsecase.execute(commentModel)
                .subscribeOn(mSchedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "saveComment onSubscribe");
                compositeDisposable.add(d);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "saveComment onComplete");
                final Disposable disposable = commentDao.loadAllByImageId(commentModel.imageId)
                        .subscribe(mCommentsLiveData::postValue);
                compositeDisposable.add(disposable);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError" + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * This method retrieves all comments for given image id and posts result to mCommentsLiveData
     * Client needs to observe mCommentsLiveData to get result.
     * @param imageId image id for which comments to be retrieved
     */
    public void getAllCommentsByImageId(String imageId) {
        mGetCommentsUseCase.execute(imageId)
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<CommentModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<CommentModel> commentModels) {
                        mCommentsLiveData.postValue(commentModels);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public void onCleared() {
        compositeDisposable.clear();
    }
}