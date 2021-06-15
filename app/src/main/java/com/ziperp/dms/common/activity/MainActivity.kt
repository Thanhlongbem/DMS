package com.ziperp.dms.common.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.R
import com.ziperp.dms.common.adapter.TableViewAdapter
import com.ziperp.dms.common.model.TableData
import com.ziperp.dms.common.model.TableViewModel
import com.ziperp.dms.common.table.TableViewListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initTableViewData()
        setupUI()

    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
    }


    private fun initTableViewData(){
        val tableViewModel =
            TableViewModel(initDummyData())
        val tableViewAdapter = TableViewAdapter()
        tbViewContent.setAdapter(tableViewAdapter)
        tbViewContent.tableViewListener =
            TableViewListener(tbViewContent)
        tbViewContent.isIgnoreSelectionColors = true
        tableViewAdapter.setAllItems(tableViewModel.columnHeaderList, tableViewModel.rowHeaderList, tableViewModel.cellList)


    }

    private fun initDummyData(): List<TableData> {
        val allData: MutableList<TableData> = ArrayList()

        val data = mutableMapOf<String, String>()
        data["title"] = "Title"
        data["id"] = "Identification"

        for (i in 2..15) {
            data["column $i"] = "Data"
        }

        for (i in 0..50) {
            allData.add(TableData(data))
        }

        return allData
    }
}
