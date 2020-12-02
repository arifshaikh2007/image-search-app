package com.sample.imagesearch.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sample.data.models.ImageDataModel;

import javax.inject.Inject;

/**
 * This class is view model class for MainActivity using which
 * {@link com.sample.imagesearch.ui.SearchFragment} and {@link com.sample.imagesearch.ui.ImageDetailsFragment}
 * communicates. {@link com.sample.imagesearch.ui.SearchFragment} sets selected image data model {@link ImageDataModel}
 * to mImageDataModelLiveData using setSelectedImageDetails method, while ImageDetailsFragment listens
 * for selected image details via mImageDataModelLiveData.
 * This class is used for tablet use case where image selected from left search fragment needs to be
 * shown in right side Image details fragment.
 */
public class SelectedImageDetailsViewModel extends ViewModel {
    private final MutableLiveData<ImageDataModel> mImageDataModelLiveData = new MutableLiveData<>();

    @Inject
    public SelectedImageDetailsViewModel() {
    }

    public void setSelectedImageDetails(ImageDataModel imageDataModel) {
        mImageDataModelLiveData.setValue(imageDataModel);
    }

    public MutableLiveData<ImageDataModel> getImageDataModelLiveData() {
        return mImageDataModelLiveData;
    }
}
