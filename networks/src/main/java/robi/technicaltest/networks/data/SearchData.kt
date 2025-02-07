package robi.technicaltest.networks.data

import android.os.Parcel
import android.os.Parcelable

data class SearchData(
    val incomplete_results: Boolean,
    val items: List<User>,
    val total_count: Int
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.createTypedArrayList(User) as List<User> ,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (incomplete_results) 1 else 0)
        parcel.writeTypedList(items)
        parcel.writeInt(total_count)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchData> {
        override fun createFromParcel(parcel: Parcel): SearchData {
            return SearchData(parcel)
        }

        override fun newArray(size: Int): Array<SearchData?> {
            return arrayOfNulls(size)
        }
    }
}