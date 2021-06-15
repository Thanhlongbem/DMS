package com.ziperp.dms.main.trackingreports.picture.view

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseAdapter
import com.ziperp.dms.camera.CustomerImage
import com.ziperp.dms.camera.CustomerImagesResponse
import com.ziperp.dms.camera.ImageModifyActivity
import com.ziperp.dms.camera.ImageModifyActivity.Companion.EXTRA_POSITION
import com.ziperp.dms.camera.ImageModifyActivity.Companion.EXTRA_VIEW_ONLY
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.reformatDate
import com.ziperp.dms.main.trackingreports.picture.model.GroupPictureOfCustomer
import kotlinx.android.synthetic.main.item_group_picture.view.*
import java.util.*

class GroupPictureAdapter: BaseAdapter<GroupPictureOfCustomer>() {

    override fun getLayoutId(): Int = R.layout.item_group_picture

    override fun getViewHolder(view: View): RecyclerView.ViewHolder = DataViewHolder(view)

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), Binder<GroupPictureOfCustomer> {
        @SuppressLint("SetTextI18n")
        override fun bind(item: GroupPictureOfCustomer) {
            itemView.apply {
                tv_visit_customer.text = item.listPicture[0].cstNm
                tv_checkin_time.text = item.listPicture[0].checkOutDay.reformatDate()
                tv_numb_pic.text = "${item.listPicture.size} ${R.string.str_images.getString()}"

                val pictureAdapter = VisitedPictureAdapter()
                pictureAdapter.updateData(item.listPicture)
                rv_images.layoutManager = GridLayoutManager(context, 3)
                rv_images.adapter = pictureAdapter
                rv_images.setHasFixedSize(true)

                val listImage = pictureAdapter.data.map { picture ->
                    CustomerImage(
                        fileNm = picture.fileNm,
                        fileLength = picture.fileLength,
                        download = picture.fileURL,
                        fileNote = picture.fileNote
                    )
                }
                pictureAdapter.onClickListener = { position ->
                    val intent = Intent(context, ImageModifyActivity::class.java)
                    intent.putExtra(ImageModifyActivity.EXTRA_IMAGE_LIST, listImage as ArrayList<CustomerImage>)
                    intent.putExtra(EXTRA_POSITION, position)
                    intent.putExtra(EXTRA_VIEW_ONLY, true)
                    context.startActivity(intent)
                }
            }
        }
    }
}