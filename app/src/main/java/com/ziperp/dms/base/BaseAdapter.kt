package com.ziperp.dms.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.setOnClickNetworkListener
import com.ziperp.dms.utils.AppUtil

abstract class BaseAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: MutableList<T> = ArrayList()
    var onClickListener: ((position: Int) -> Unit)? = null
    var deleteFunction: ((position: Int)-> Unit)? = null
    var needToCheckNetwork = true
    var context: Context? = null
    abstract fun getLayoutId(): Int
    abstract fun getViewHolder(view: View):RecyclerView.ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(getLayoutId(), parent, false)
        return getViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as Binder<T>).bind(data[position])
        if (needToCheckNetwork) {
            holder.itemView.setOnClickNetworkListener { onClickListener?.invoke(position) }
        } else {
            holder.itemView.setOnClickListener { onClickListener?.invoke(position) }
        }
        (holder.itemView.findViewById<TextView>(R.id.btn_delete))?.setOnClickListener {
            AppUtil.showAlertDialog(context, R.string.str_question_delete.getString(), {
                deleteFunction?.invoke(position)
            }, {})
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    open fun addData(list: List<T>) {
        data.addAll(list)
        notifyDataSetChanged()
    }

    open fun updateData(list: List<T>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }


    open fun getItem(position: Int): T{
        return data[position]
    }

    fun removeAll() {
        data.clear()
        notifyDataSetChanged()
    }

    internal interface Binder<T> {
        fun bind(item: T)
    }
}