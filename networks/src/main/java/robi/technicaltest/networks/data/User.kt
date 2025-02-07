package robi.technicaltest.networks.data

import android.os.Parcel
import android.os.Parcelable

data class User(
    val avatar_url: String?,
    val bio: String?,
    val blog: String?,
    val company: String?,
    val created_at: String?,
    val email: String?,
    val events_url: String?,
    val followers: Int,
    val followers_url: String?,
    val following: Int,
    val following_url: String?,
    val gists_url: String?,
    val gravatar_id: String?,
    val hireable: String?,
    val html_url: String?,
    val id: Int,
    val location: String?,
    val login: String?,
    val name: String?,
    val node_id: String?,
    val organizations_url: String?,
    val public_gists: Int,
    val public_repos: Int,
    val received_events_url: String?,
    val repos_url: String?,
    val site_admin: Boolean,
    val starred_url: String?,
    val subscriptions_url: String?,
    val twitter_username: String?,
    val type: String?,
    val updated_at: String?,
    val url: String?,
    val user_view_type: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(avatar_url)
        parcel.writeString(blog)
        parcel.writeString(company)
        parcel.writeString(created_at)
        parcel.writeString(email)
        parcel.writeString(events_url)
        parcel.writeInt(followers)
        parcel.writeString(followers_url)
        parcel.writeInt(following)
        parcel.writeString(following_url)
        parcel.writeString(gists_url)
        parcel.writeString(gravatar_id)
        parcel.writeString(hireable)
        parcel.writeString(html_url)
        parcel.writeInt(id)
        parcel.writeString(location)
        parcel.writeString(login)
        parcel.writeString(name)
        parcel.writeString(node_id)
        parcel.writeString(organizations_url)
        parcel.writeInt(public_gists)
        parcel.writeInt(public_repos)
        parcel.writeString(received_events_url)
        parcel.writeString(repos_url)
        parcel.writeByte(if (site_admin) 1 else 0)
        parcel.writeString(starred_url)
        parcel.writeString(subscriptions_url)
        parcel.writeString(twitter_username)
        parcel.writeString(type)
        parcel.writeString(updated_at)
        parcel.writeString(url)
        parcel.writeString(user_view_type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}