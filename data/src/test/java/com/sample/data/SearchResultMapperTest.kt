package com.sample.data

import com.sample.data.mappers.ImageDetailsMapper
import com.sample.data.mappers.SearchResultMapper
import com.sample.data.models.ImageDataModel
import com.sample.data.models.ResponseModel
import com.sample.data.models.SearchResultDataModel
import com.sample.domain.models.SearchError
import com.sample.domain.models.SearchResultModel
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SearchResultMapperTest {
    private var searchResultMapper: SearchResultMapper? = null

    @Before
    fun setUp() {
        searchResultMapper = SearchResultMapper()
    }

    @After
    fun tearDown() {
        searchResultMapper = null
    }

    @Test
    fun testToSearchResultSuccess() {
        val images1 = listOf(ImageDataModel("1", "http://www.imgur.com/eqede4.jpg", "image", ""),
                             ImageDataModel("2", "http://www.imgur.com/aefasd.jpg", "image", ""),
                             ImageDataModel("3", "http://www.imgur.com/rgsfg.jpg", "image", ""))

        val images2 = listOf(ImageDataModel("4", "http://www.imgur.com/eqede4.jpg", "image", ""))

        val dataList = listOf(SearchResultDataModel(images1, "title1"), SearchResultDataModel(images2, "title2"))
        val searchResultModel = ResponseModel(dataList, true, 200)
        val searchResult = searchResultMapper!!.toSearchResult(ImageDetailsMapper(), searchResultModel)

        assertNotNull(searchResult)
        assertTrue(searchResult is SearchResultModel.Success)

        val successResult = (searchResult as SearchResultModel.Success)
        assertTrue(successResult.imageList.size == 4)

        assertTrue(successResult.imageList[0].thumbnailUrl == "http://www.imgur.com/eqede4s.jpg")
        assertTrue(successResult.imageList[0].title == "title1")

        assertTrue(successResult.imageList[1].thumbnailUrl == "http://www.imgur.com/aefasds.jpg")
        assertTrue(successResult.imageList[1].title == "title1")

        assertTrue(successResult.imageList[2].thumbnailUrl == "http://www.imgur.com/rgsfgs.jpg")
        assertTrue(successResult.imageList[2].title == "title1")

        assertTrue(successResult.imageList[3].thumbnailUrl == "http://www.imgur.com/eqede4s.jpg")
        assertTrue(successResult.imageList[3].title == "title2")
    }

    @Test
    fun testToSearchResultError() {
        val searchResultModel = ResponseModel(emptyList(), false, 503)
        val searchResult = searchResultMapper!!.toSearchResult(ImageDetailsMapper(), searchResultModel)

        assertNotNull(searchResult)
        assertTrue(searchResult is SearchResultModel.Failure)

        val failureResult = ((searchResult as SearchResultModel.Failure).searchError) as SearchError.ServerError
        assertTrue(failureResult.errorCode == 503)
    }
}