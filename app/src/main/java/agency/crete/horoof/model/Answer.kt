package agency.crete.horoof.model

import android.os.Parcel
import android.os.Parcelable

class Answer(id: Int, content: String, status: Boolean): Parcelable {

    var id = id
    var content = content
    var status = status

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt() == 1
    ) {
        id = parcel.readInt()
        content = parcel.readString()
        status = parcel.readInt() == 1
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(content)
        parcel.writeInt(if (status) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Answer> {
        override fun createFromParcel(parcel: Parcel): Answer {
            return Answer(parcel)
        }

        override fun newArray(size: Int): Array<Answer?> {
            return arrayOfNulls(size)
        }
    }
}