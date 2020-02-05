package com.bleacher.reportclifford.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BaseTransientBottomBar.LENGTH_LONG
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import com.bleacher.reportclifford.R
import com.bleacher.reportclifford.ui.ImageActivity.Companion.IMAGE_DESCRIPTION_EXTRA
import com.bleacher.reportclifford.ui.ImageActivity.Companion.IMAGE_TITLE_EXTRA
import com.bleacher.reportclifford.ui.ImageActivity.Companion.IMAGE_URL_EXTRA
import com.bleacher.reportclifford.list.api.FlickrImageService
import com.bleacher.reportclifford.list.api.FlickrImageService.Companion.BASE_URL
import com.bleacher.reportclifford.list.api.RetrofitApi
import com.bleacher.reportclifford.Data.PhotoSearchResultItem
import com.bleacher.reportclifford.list.ImageAdapter
import com.bleacher.reportclifford.list.ImageListPresenter
import com.bleacher.reportclifford.list.ImageListView
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_image_list.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ImageListActivity : AppCompatActivity(), ImageListView {

    private var presenter: ImageListPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_image_list)
        setSupportActionBar(appBar)

        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(OkHttpClient())
                .build()

        val apiService = retrofit.create(RetrofitApi::class.java)
        val imageService = FlickrImageService(apiService)

        this.presenter = ImageListPresenter(this, imageService, mainThread(), Schedulers.io())

        if(savedInstanceState == null) {
            this.presenter!!.requestImages()
        }

        initialiseSearchView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_images_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return super.onOptionsItemSelected(item)
    }



    override fun onDestroy() {
        super.onDestroy()
        this.presenter!!.onDestroy()
    }

    override fun showLoading(show: Boolean) {
        loadingOverlay.visibility = if (show) VISIBLE else GONE
    }

    override fun showConnectionError() {
        Snackbar.make(imagesList, R.string.check_connection, LENGTH_LONG).show()
    }

    override fun showGenericError() {
        Snackbar.make(imagesList, R.string.error_retrieving_results, LENGTH_LONG).show()
    }

    private fun initialiseSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String): Boolean {
                hideKeyboard()
                presenter!!.requestImages(text)
                return true
            }

            override fun onQueryTextChange(text: String): Boolean {
                return true
            }
        })
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchView.windowToken, 0)
    }

    override fun showImages(images: List<PhotoSearchResultItem>) {
        val columns = 2

        val adapter = ImageAdapter(images, columns)
        adapter.setOnImageClickListener(object : ImageAdapter.OnPhotoSearchResultClickListener {
            override fun onImageClicked(image: PhotoSearchResultItem) {
                val intent = Intent(this@ImageListActivity, ImageActivity::class.java)
                intent.putExtra(IMAGE_URL_EXTRA, image.url)
                intent.putExtra(IMAGE_TITLE_EXTRA, image.title)
                intent.putExtra(IMAGE_DESCRIPTION_EXTRA, image.toString())

                startActivity(intent)
            }
        })

        imagesList.adapter = adapter
        imagesList.layoutManager = GridLayoutManager(this@ImageListActivity, columns)
    }

    companion object {
        private val DATE_TAKEN_DIALOG_OPTION_POSITION = 1
    }
}