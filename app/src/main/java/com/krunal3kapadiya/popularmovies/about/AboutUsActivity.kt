package com.krunal3kapadiya.popularmovies.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.krunal3kapadiya.popularmovies.R
import kotlinx.android.synthetic.main.activity_about.*

/**
 * @author krunal kapadiya
 * @link https://krunal3kapadiya.app
 * @date 14,April,2019
 */

class AboutUsActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, AboutUsActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        about_txt.text = getString(R.string.about_website)
//        BundledEmojiCompatConfig(this)
//                .apply {
//                    EmojiCompat.init(this)
//                            .apply {
//                                process(getString(R.string.about_website))
//                                        .apply {
//                                            = this
//                                        }
//                            }
//                }
    }
}