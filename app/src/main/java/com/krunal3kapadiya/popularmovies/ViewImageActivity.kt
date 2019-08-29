package com.krunal3kapadiya.popularmovies

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_view_image.*
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.squareup.picasso.Picasso
import android.provider.MediaStore.Images.Media.getBitmap
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.io.File
import java.io.FileOutputStream


class ViewImageActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE = 102

    companion object {
        fun launch(context: Context, url: String?) {
            val intent = Intent(context, ViewImageActivity::class.java)
            intent.putExtra("string", url)
            context.startActivity(intent)
        }

        fun launch(context: Context, bitmap: Bitmap?) {
            val intent = Intent(context, ViewImageActivity::class.java)
            intent.putExtra("bitmap", bitmap)
            context.startActivity(intent)
        }
    }

    lateinit var mBitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)
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

        val bitmap = intent.getParcelableExtra<Bitmap>("bitmap")
        bitmap?.let {
            mBitmap = it
            Glide.with(this).load(it)
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
                val bitmap = (image_full_screen.drawable as BitmapDrawable).bitmap
//                shareBitmap(bitmap)
                shareImage()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun shareImage() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
//        //TODO change file path
        val bitmap = (image_full_screen.drawable as BitmapDrawable).bitmap
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra(Intent.EXTRA_STREAM,
                getImageUri(this, bitmap)
//                FileProvider.getUriForFile(this, "$packageName.provider", "")
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

    private fun shareBitmap(bitmap: Bitmap) {

        val shareText = " " + getString(R.string.app_name) + " developed by https://play.google.com/store/apps/details?id=" + packageName + ": \n\n"
        try {
            val file = File(this.externalCacheDir, "share.png")
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()
            file.setReadable(true, false)
            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(Intent.EXTRA_TEXT, shareText)
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
            intent.type = "image/png"
            startActivity(Intent.createChooser(intent, "Share image via"))

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun getImageUri(context: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }
}
