package com.sample.data;

import com.sample.data.mappers.ImageDetailsMapper;
import com.sample.data.models.ImageDataModel;
import com.sample.domain.models.ImageDetailsModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ImageDetailsMapperTest {
    private ImageDetailsMapper mImageDetailsMapper;

    @Before
    public void setUp() {
        mImageDetailsMapper = new ImageDetailsMapper();
    }

    @After
    public void tearDown() {
        mImageDetailsMapper = null;
    }

    @Test
    public void testToImageDetailsModel() {
        final ImageDataModel imageDataModel = new ImageDataModel("1", "http://www.imgur.com/eqede4.jpg", "image", "title");

        final ImageDetailsModel imageDetailsModel = mImageDetailsMapper.toImageDetailsModel(imageDataModel);

        assertEquals(imageDataModel.getId(), imageDetailsModel.getId());
        assertEquals(imageDataModel.getUrl(), imageDetailsModel.getUrl());
        assertEquals(imageDataModel.getType(), imageDetailsModel.getType());
        assertEquals(imageDataModel.getTitle(), imageDetailsModel.getTitle());
        assertEquals("http://www.imgur.com/eqede4s.jpg", imageDetailsModel.getThumbnailUrl());
    }

    @Test
    public void testToImageDataModel() {
        final ImageDetailsModel imageDetailsModel = new ImageDetailsModel("1", "http://www.imgur.com/eqede4.jpg", "","image", "title");

        final ImageDataModel imageDataModel = mImageDetailsMapper.toImageDataModel(imageDetailsModel);

        assertEquals(imageDataModel.getId(), imageDetailsModel.getId());
        assertEquals(imageDataModel.getUrl(), imageDetailsModel.getUrl());
        assertEquals(imageDataModel.getType(), imageDetailsModel.getType());
        assertEquals(imageDataModel.getTitle(), imageDetailsModel.getTitle());
        assertEquals("", imageDetailsModel.getThumbnailUrl());
    }
}
