package com.krunal3kapadiya.popularmovies

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.krunal3kapadiya.popularmovies.dashBoard.DashBoardActivity

/**
 * @author krunal kapadiya
 * @link https://krunal3kapadiya.app
 * @date 14,April,2019
 */

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            DashBoardActivity.launch(this)
            this.finish()
        }, 2000)
    }
}