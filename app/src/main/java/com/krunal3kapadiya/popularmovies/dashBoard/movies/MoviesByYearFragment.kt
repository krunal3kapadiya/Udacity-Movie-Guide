package com.krunal3kapadiya.popularmovies.dashBoard.movies

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast

import com.krunal3kapadiya.popularmovies.R
import kotlinx.android.synthetic.main.movies_by_year_fragment.*
import java.util.*
import kotlin.collections.ArrayList

class MoviesByYearFragment : Fragment() {

    companion object {
        fun newInstance() = MoviesByYearFragment()
    }

    private lateinit var viewModel: MoviesByYearViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.movies_by_year_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MoviesByYearViewModel::class.java)

        val year = Calendar.getInstance().get(Calendar.YEAR)
        val reversedyearList: ArrayList<String> = ArrayList()
        val yearList: ArrayList<String> = ArrayList()
        yearList.add("Select year")
        for (i in 1950..year) {
            reversedyearList.add(i.toString())
        }
        yearList.addAll(reversedyearList.asReversed())
        val adapter = ArrayAdapter(this.activity, android.R.layout.simple_spinner_item, yearList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_year_list.adapter = adapter
        spinner_year_list.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val selectedYear = yearList[i]
                Toast.makeText(activity, selectedYear, Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }
        // TODO: Use the ViewModel
    }
}
