package com.bleacher.reportclifford.list.api;

import com.bleacher.reportclifford.Data.PhotoSearch;
import com.bleacher.reportclifford.Data.PhotoSearchResultItem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FlickrImageServiceTest {

    @Mock
    private RetrofitApi apiService;

    @InjectMocks
    private FlickrImageService flickrImageService;

    private Date dateNow = new Date();

    @Test
    public void shouldOrderPhotosByDatePublishedByDefaultWhenListingPhotos() {
        ArrayList<PhotoSearchResultItem> searchResultItems = new ArrayList<>();

        final PhotoSearchResultItem firstPublishedPhoto = new PhotoSearchResultItem("published 1st", null, null, yesterday(), yesterday());
        final PhotoSearchResultItem secondPublishedPhoto = new PhotoSearchResultItem("published 2nd", null, null, yesterday(), dateNow);

        searchResultItems.add(firstPublishedPhoto);
        searchResultItems.add(secondPublishedPhoto);

        PhotoSearch photoSearch = new PhotoSearch(null, searchResultItems);

        when(apiService.listPhotos())
                .thenReturn(Single.just(photoSearch));

        flickrImageService.listPhotos()
                .test()
                .assertValueAt(0, new Predicate<PhotoSearch>() {
                    @Override
                    public boolean test(@NonNull PhotoSearch searchResult) throws Exception {
                        return searchResult.getItems().get(0).equals(secondPublishedPhoto);
                    }
                })
                .assertValueAt(0, new Predicate<PhotoSearch>() {
                    @Override
                    public boolean test(@NonNull PhotoSearch searchResult) throws Exception {
                        return searchResult.getItems().get(1).equals(firstPublishedPhoto);
                    }
                });
    }

    @Test
    public void shouldOrderPhotosByDatePublishedByDefaultWhenSearchingByTag() {
        ArrayList<PhotoSearchResultItem> searchResultItems = new ArrayList<>();

        final PhotoSearchResultItem firstPublishedPhoto = new PhotoSearchResultItem("published 1st", null, null, yesterday(), yesterday());
        final PhotoSearchResultItem secondPublishedPhoto = new PhotoSearchResultItem("published 2nd", null, null, yesterday(), dateNow);

        searchResultItems.add(firstPublishedPhoto);
        searchResultItems.add(secondPublishedPhoto);

        PhotoSearch photoSearch = new PhotoSearch(null, searchResultItems);
        String searchTerm = "dogs";

        when(apiService.searchPhotos(eq(searchTerm)))
                .thenReturn(Single.just(photoSearch));

        flickrImageService.searchPhotos(searchTerm)
                .test()
                .assertValueAt(0, new Predicate<PhotoSearch>() {
                    @Override
                    public boolean test(@NonNull PhotoSearch searchResult) throws Exception {
                        return searchResult.getItems().get(0).equals(secondPublishedPhoto);
                    }
                })
                .assertValueAt(0, new Predicate<PhotoSearch>() {
                    @Override
                    public boolean test(@NonNull PhotoSearch searchResult) throws Exception {
                        return searchResult.getItems().get(1).equals(firstPublishedPhoto);
                    }
                });
    }

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

}