package com.sample.imagesearch.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.sample.data.mappers.ImageDetailsMapper
import com.sample.data.models.ImageDataModel
import com.sample.domain.models.ImageDetailsModel
import com.sample.domain.models.SearchError
import com.sample.domain.models.SearchResultModel
import com.sample.domain.models.SearchResultModel.Success
import com.sample.imagesearch.R
import com.sample.imagesearch.di.obtainViewModel
import com.sample.imagesearch.rx.SearchViewObservable
import com.sample.imagesearch.viewmodel.SearchViewModel
import com.sample.imagesearch.viewmodel.SelectedImageDetailsViewModel
import com.squareup.picasso.Picasso
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchFragment : Fragment(), ImageListAdapter.SelectItemCallback {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewmodel : SearchViewModel
    private lateinit var selectedImageViewModel : SelectedImageDetailsViewModel
    private var searchResultAdapter: ImageListAdapter? = null

    private val compositeDisposable = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        viewmodel = viewModelFactory.obtainViewModel<SearchViewModel>(this)
        selectedImageViewModel = viewModelFactory.obtainViewModel<SelectedImageDetailsViewModel>(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.searchLiveData.observe(viewLifecycleOwner, Observer {
            it?.apply {  handleSearchResult(requireContext(), it) }
        })
        observeSearchView(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //save last searched term so that network query is not made on device rotation
        outState.putString(LAST_SEARCHED_QUERY, search_bar_view_top.query.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchResultAdapter = null
        compositeDisposable.clear()
    }

    private fun observeSearchView(savedInstanceState: Bundle?) {
        val searchView = view?.findViewById<SearchView>(R.id.search_bar_view_top)
        searchView?.isIconified = false
        searchView?.requestFocusFromTouch()
        searchView?.let {
            val disposable = SearchViewObservable.fromView(it)
                .debounce(DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
                .filter { text ->
                    !(text.isBlank() || (savedInstanceState != null
                            && savedInstanceState.get(LAST_SEARCHED_QUERY)!! == text))
                }
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    setVisibleView(R.id.loading_progress)
                    viewmodel.getSearchResult(it)
                }
            compositeDisposable.add(disposable)
        }
    }

    private fun handleSearchResult(context: Context, searchResultModel: SearchResultModel) {
        if (view == null) {
            return
        }

        when(searchResultModel) {
            is Success -> {
                when {
                    searchResultModel.imageList.isEmpty() -> {
                        setVisibleView(R.id.message_txt)
                        view?.findViewById<TextView>(R.id.message_txt)?.setText(R.string.empty_search_result)
                    }
                    searchResultAdapter == null -> {
                        setVisibleView(R.id.search_result_grid)
                        setSearchResultAdapter(searchResultModel.imageList)
                    }
                    else -> {
                        setVisibleView(R.id.search_result_grid)
                        searchResultAdapter!!.setImageList(searchResultModel.imageList)
                    }
                }
            }
            is SearchResultModel.Failure -> {
                setVisibleView(R.id.message_txt)
                showErrorText(searchResultModel.searchError)
            }
        }
    }

    private fun setSearchResultAdapter(imageList: List<ImageDetailsModel>) {
        val searchResultGrid = view?.findViewById<RecyclerView>(R.id.search_result_grid)
        searchResultAdapter = ImageListAdapter(imageList = imageList, selectItemCallback = this)
        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.flexWrap = FlexWrap.WRAP
        layoutManager.justifyContent = JustifyContent.CENTER

        searchResultGrid?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
        searchResultGrid?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        searchResultGrid?.layoutManager = layoutManager
        searchResultGrid?.adapter = searchResultAdapter
    }

    private fun showErrorText(searchError: SearchError) {
        val messageText = view?.findViewById<TextView>(R.id.message_txt)
        when(searchError) {
            is SearchError.NetworkError -> {
                messageText?.setText(R.string.network_error_while_searching)
            }
            is SearchError.ServerError -> {
                messageText?.setText(R.string.server_error_while_searching)
            }
        }
    }

    private fun setVisibleView(visibleViewId: Int) {
        val viewIdList = listOf(R.id.loading_progress, R.id.message_txt, R.id.search_result_grid)
        val visibleView = listOf(visibleViewId)
        val goneViewIds = viewIdList - visibleView

        visibleView.forEach { requireView().findViewById<View>(it).visibility = View.VISIBLE }
        goneViewIds.forEach { requireView().findViewById<View>(it).visibility = View.GONE }
    }

    override fun onImageSelected(imageDataModel: ImageDataModel) {
        selectedImageViewModel.setSelectedImageDetails(imageDataModel)
    }

    companion object {
        const val LAST_SEARCHED_QUERY = "last_searched_query"
        const val DEBOUNCE_TIMEOUT = 250L
    }
}

class ImageListAdapter(private var imageList: List<ImageDetailsModel>, private var selectItemCallback: SelectItemCallback)
    : RecyclerView.Adapter<ImageListAdapter.ImageViewHolder>() {

    interface SelectItemCallback {
        fun onImageSelected(imageDataModel: ImageDataModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_image_details, parent, false)
        return ImageViewHolder(inflatedView, selectItemCallback)
    }

    override fun getItemCount(): Int {
       return imageList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bindImage(imageList[position])
        holder.setIsRecyclable(false)
    }

    fun setImageList(newImages: List<ImageDetailsModel>) {
        imageList = newImages
        notifyDataSetChanged()
    }

    class ImageViewHolder(v: View, private var selectItemCallback: SelectItemCallback) : RecyclerView.ViewHolder(v), View.OnClickListener  {
        private var imageView: ImageView = v.findViewById(R.id.searched_result_image_item)
        private var imageDetailsModel: ImageDetailsModel? = null
        init {
            imageView.setOnClickListener(this)
        }

        fun bindImage(image: ImageDetailsModel) {
            this.imageDetailsModel = image
            Picasso.get().load(image.thumbnailUrl).fit().centerCrop().noFade().placeholder(R.drawable. progress_animation).into(imageView)
            Log.d(TAG, "picasso downloadeding image:" + image.thumbnailUrl)
        }

        override fun onClick(v: View?) {
            val view = imageView.rootView.findViewById<View>(R.id.tablet_fragment_container)
            val imageDataModel = ImageDetailsMapper().toImageDataModel(imageDetailsModel!!)
            if (view == null) {
                val intent = Intent(v?.context, ImageDetailsActivity::class.java)
                intent.putExtra("image_details", imageDataModel)
                v?.context?.startActivity(intent)
            }
            selectItemCallback.onImageSelected(imageDataModel)
        }

        companion object {
            const val TAG = "ImageListAdapter"
        }
    }
}