package com.krunal3kapadiya.popularmovies.dashBoard

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.krunal3kapadiya.popularmovies.Constants
import com.krunal3kapadiya.popularmovies.R
import kotlinx.android.synthetic.main.activity_view_image.*
import java.io.ByteArrayOutputStream


class ViewImageActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE = 102

    companion object {
        fun launch(context: Context, url: String?) {
            val intent = Intent(context, ViewImageActivity::class.java)
            intent.putExtra("string", url)
            context.startActivity(intent)
        }
    }

    lateinit var mBitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)
        supportActionBar?.title = ""

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val url = intent.getStringExtra("string")
        url?.let {
            Glide.with(this)
                    .asBitmap()
                    .load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE + it)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            image_full_screen.setImageBitmap(resource)
                            mBitmap = resource
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            // this is called when imageView is cleared on lifecycle call or for
                            // some other reason.
                            // if you are referencing the bitmap somewhere else too other than this imageView
                            // clear it here as you can no longer have the bitmap
                        }
                    })



            Glide.with(this).load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE_500 + it)
                    .into(image_full_screen)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_image_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_save_image -> {
                val bitmap = (image_full_screen.drawable as BitmapDrawable).bitmap
                MediaStore.Images.Media.insertImage(contentResolver, bitmap, "image.jpeg", null)
                true
            }
            R.id.action_set_as -> {
                if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                        != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            PERMISSION_REQUEST_CODE
                    )
                } else {
                    val bitmap = (image_full_screen.drawable as BitmapDrawable).bitmap
                    val intent = Intent(Intent.ACTION_ATTACH_DATA)
                            .apply {
                                addCategory(Intent.CATEGORY_DEFAULT)
                                setDataAndType(getImageUri(this@ViewImageActivity, bitmap), "image/*")
                                putExtra("mimeType", "image/*")
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                    startActivity(Intent.createChooser(intent, "Set as:"))
                }

                true
            }
            R.id.action_share_image -> {
                // Share image call back
                shareImage()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun shareImage() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        val bitmap = (image_full_screen.drawable as BitmapDrawable).bitmap
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra(Intent.EXTRA_STREAM,
                getImageUri(this, bitmap)
        )
        startActivity(Intent.createChooser(intent, "Share with..."))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE ->
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val bitmap = (image_full_screen.drawable as BitmapDrawable).bitmap
                    val intent = Intent(Intent.ACTION_ATTACH_DATA)
                            .apply {
                                addCategory(Intent.CATEGORY_DEFAULT)
                                setDataAndType(getImageUri(this@ViewImageActivity, bitmap), "image/*")
                                putExtra("mimeType", "image/*")
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                    startActivity(Intent.createChooser(intent, "Set as:"))
                } else {
                    finish()
                }
        }
    }

    fun getImageUri(context: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, inImage,
                "Title",
                null)
        return Uri.parse(path)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}
