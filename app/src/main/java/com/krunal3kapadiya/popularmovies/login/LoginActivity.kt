package com.krunal3kapadiya.popularmovies.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.krunal3kapadiya.popularmovies.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.elevation = 0F
        val viewModel = ViewModelProviders.of(this).get(LoginviewModel::class.java)
        viewModel.getRequestToken().observe(this, Observer {
            Toast.makeText(this@LoginActivity, it, Toast.LENGTH_LONG).show()
            editText.setText(it)
        })
    }
}
