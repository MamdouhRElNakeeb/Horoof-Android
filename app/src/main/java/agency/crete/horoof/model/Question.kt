package agency.crete.horoof.model

import android.os.Parcel
import android.os.Parcelable

class Question(id: Int, content: String, answers: List<Answer>): Parcelable {

    val id = id
    val content = content
    val answers = answers

    private constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        arrayListOf<Answer>().apply {
            parcel.readList(this, Answer::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(content)
        parcel.writeList(answers)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}