package com.ziperp.dms.common.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem


class MarkerItem : ClusterItem {
    private val mPosition: LatLng
    private val mTitle: String
    private val mSnippet: String
    private val mProfilePhoto: Int
    private val mAddress: String
    private val mRepPerson: String
    private val mPhone: String
    private val mDistance: String
    private val mIsCurrentPosition: Boolean

    constructor(lat: Double, lng: Double, title: String, snippet: String, profilePhoto: Int, address: String, repName: String, phone: String, distance: String, isCurrentPosition: Boolean) {
        mPosition = LatLng(lat, lng)
        mTitle = title
        mSnippet = snippet
        mProfilePhoto = profilePhoto
        mAddress = address
        mRepPerson = repName
        mPhone = phone
        mDistance = distance
        mIsCurrentPosition = isCurrentPosition
    }

    override fun getPosition(): LatLng {
        return mPosition
    }

    override fun getTitle(): String {
        return mTitle
    }

    override fun getSnippet(): String {
        return mSnippet
    }

    fun getAddress(): String {
        return mAddress
    }

    fun getRepName(): String {
        return mRepPerson
    }

    fun getPhone(): String {
        return mPhone
    }

    fun getDistance(): String {
        return mDistance
    }

    fun isCurrentPosition(): Boolean {
        return mIsCurrentPosition
    }
}