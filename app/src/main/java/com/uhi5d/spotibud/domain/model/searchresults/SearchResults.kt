package com.uhi5d.spotibud.domain.model.searchresults


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.uhi5d.spotibud.domain.model.ExternalIds
import com.uhi5d.spotibud.domain.model.ExternalUrls
import com.uhi5d.spotibud.domain.model.Followers
import com.uhi5d.spotibud.domain.model.Image
import kotlinx.android.parcel.Parcelize

data class SearchResults(
    val albums: SearchResultsAlbums?,
    val artists: SearchResultsArtists?,
    val tracks: SearchResultsTracks?
)

data class SearchResultsAlbums(
    val href: String?,
    val items: List<SearchResultsAlbumsItem>?,
    val limit: Int?,
    val next: String?,
    val offset: Int?,
    val previous: Any?,
    val total: Int?
)
@Parcelize
data class SearchResultsAlbumsItem(
    @SerializedName("album_type")
    val albumType: String?,
    val artists: List<SearchResultsAlbumsItemArtist>?,
    @SerializedName("available_markets")
    val availableMarkets: List<String>?,
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls?,
    val href: String?,
    val id: String?,
    val images: List<Image>?,
    val name: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("release_date_precision")
    val releaseDatePrecision: String?,
    @SerializedName("total_tracks")
    val totalTracks: Int?,
    val type: String?,
    val uri: String?
): Parcelable

data class SearchResultsAlbumsItemArtist(
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls?,
    val href: String?,
    val id: String?,
    val name: String?,
    val type: String?,
    val uri: String?
)

data class SearchResultsArtists(
    val href: String?,
    val items: List<SearchResultsArtistsItem>?,
    val limit: Int?,
    val next: String?,
    val offset: Int?,
    val previous: Any?,
    val total: Int?
)
@Parcelize
data class SearchResultsArtistsItem(
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls?,
    val followers: Followers?,
    val genres: List<String>?,
    val href: String?,
    val id: String?,
    val images: List<Image>?,
    val name: String?,
    val popularity: Int?,
    val type: String?,
    val uri: String?
):Parcelable

data class SearchResultsTracks(
    val href: String?,
    val items: List<SearchResultsTracksItem>?,
    val limit: Int?,
    val next: String?,
    val offset: Int?,
    val previous: Any?,
    val total: Int?
)
@Parcelize
data class SearchResultsTracksItem(
    val album: SearchResultsAlbumsItem?,
    val artists: List<SearchResultsArtists>?,
    @SerializedName("available_markets")
    val availableMarkets: List<String>?,
    @SerializedName("disc_number")
    val discNumber: Int?,
    @SerializedName("duration_ms")
    val durationMs: Int?,
    val explicit: Boolean?,
    @SerializedName("external_ids")
    val externalIds: ExternalIds?,
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls?,
    val href: String?,
    val id: String?,
    @SerializedName("is_local")
    val isLocal: Boolean?,
    val name: String?,
    val popularity: Int?,
    @SerializedName("preview_url")
    val previewUrl: Any?,
    @SerializedName("track_number")
    val trackNumber: Int?,
    val type: String?,
    val uri: String?
): Parcelable