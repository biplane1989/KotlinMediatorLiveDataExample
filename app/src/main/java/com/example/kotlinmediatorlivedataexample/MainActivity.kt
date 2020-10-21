package com.example.kotlinmediatorlivedataexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG = "giangtd"
    val listA: MutableLiveData<List<Int>> = MutableLiveData()
    val listB: MutableLiveData<List<String>> = MutableLiveData()
    val list = ArrayList<Orange>()
    val _listA: ArrayList<Int> = ArrayList<Int>()
    val _listB: ArrayList<String> = ArrayList<String>()

    val _listACopy: ArrayList<Int> = ArrayList<Int>()
    val _listBCopy: ArrayList<String> = ArrayList<String>()

    val listTotal = ArrayList<Orange>()

    init {
        _listA.add(1)
        _listA.add(2)
        _listA.add(3)
        _listA.add(3)

        _listB.add("apple")
        _listB.add("apple")
        _listB.add("apple")

        listB.postValue(_listB)
        listA.postValue(_listA)
    }

    val mediatorObserver = Observer<List<Orange>> {

        for (item in it) {
            Log.d(TAG, "Orange id : " + item.id + " name : " + item.name)
        }
        Log.d(TAG, "size : " + it.size)
    }

    private fun mergeList(){
        list.clear()
        _listACopy.forEach {
            list.add(Orange(it, "${it}"))
        }
        _listBCopy.forEach {
            list.add(Orange(1, it))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val mediatorLiveData = MediatorLiveData<MutableList<Orange>>()

        mediatorLiveData.addSource(listA) {
            _listACopy.clear()
            _listACopy.addAll(it)
            mergeList()
            mediatorLiveData.postValue(list)
        }
        mediatorLiveData.addSource(listB) {
            _listBCopy.clear()
            _listBCopy.addAll(it)
            mergeList()
            mediatorLiveData.postValue(list)
        }

//        list.clear()
//        list.addAll(listCopy)
//        mediatorLiveData.postValue(list)

        mediatorLiveData.observe(this, mediatorObserver)

        btn_add.setOnClickListener(View.OnClickListener {
            _listA.add(9)
            listA.postValue(_listA)

        })

        btn_delete.setOnClickListener(View.OnClickListener {
            listTotal.removeAt(0)
            mediatorLiveData.postValue(listTotal)
        })
    }
}

data class Orange(val id: Int, val name: String)

