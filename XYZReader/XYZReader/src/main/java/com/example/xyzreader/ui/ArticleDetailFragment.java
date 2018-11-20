package com.example.xyzreader.ui;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.xyzreader.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.xyzreader.data.ItemsContract.ItemsColumns.AUTHOR;
import static com.example.xyzreader.data.ItemsContract.ItemsColumns.BODY;
import static com.example.xyzreader.data.ItemsContract.ItemsColumns.PHOTO_URL;
import static com.example.xyzreader.data.ItemsContract.ItemsColumns.PUBLISHED_DATE;
import static com.example.xyzreader.data.ItemsContract.ItemsColumns.TITLE;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends Fragment {
    private static final String TAG = ArticleDetailFragment.class.getSimpleName();

    public String mPublishedDate;
    public String mTitle;
    public String mAuthor;
    public String mBody;
    public String mPhotoUrl;

    private View mRootView;
    private ImageView mPhotoView;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
    // Use default locale format
    private SimpleDateFormat outputFormat = new SimpleDateFormat();
    // Most time functions can only handle 1902 - 2037
    private GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2, 1, 1);

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    public static ArticleDetailFragment newInstance(String publishedDate,
                                                    String title,
                                                    String author,
                                                    String body,
                                                    String photoUrl) {
        Bundle arguments = new Bundle();
        arguments.putString(PUBLISHED_DATE, publishedDate);
        arguments.putString(TITLE, title);
        arguments.putString(AUTHOR, author);
        arguments.putString(BODY, body);
        arguments.putString(PHOTO_URL, photoUrl);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        mPublishedDate = bundle.getString(PUBLISHED_DATE);
        mTitle = bundle.getString(TITLE);
        mAuthor = bundle.getString(AUTHOR);
        mBody = bundle.getString(BODY);
        mPhotoUrl = bundle.getString(PHOTO_URL);


        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_article_detail, container, false);
        mPhotoView = mRootView.findViewById(R.id.photo);

        mCollapsingToolbarLayout = mRootView.findViewById(R.id.detail_collapsing_toolbar_layout);

        mToolbar = mRootView.findViewById(R.id.detail_toolbar);
        mToolbar.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        mRootView.findViewById(R.id.share_fab).setOnClickListener(view -> startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setText("Some sample text")
                .getIntent(), getString(R.string.action_share))));

        bindViews();
        return mRootView;
    }

    private Date parsePublishedDate() {
        try {
            String date = mPublishedDate;
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            Log.e(TAG, ex.getMessage());
            Log.i(TAG, "passing today's date");
            return new Date();
        }
    }

    private void bindViews() {
        if (mRootView == null) {
            return;
        }

        TextView titleView = mRootView.findViewById(R.id.article_title);
        TextView bylineView = mRootView.findViewById(R.id.article_byline);
        bylineView.setMovementMethod(new LinkMovementMethod());
        TextView bodyView = mRootView.findViewById(R.id.article_body);


        bodyView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Rosario-Regular.ttf"));

        if (mPublishedDate != null) {
            mRootView.setAlpha(0);
            mRootView.setVisibility(View.VISIBLE);
            mRootView.animate().alpha(1);
            String articleTitle = mTitle;
            titleView.setText(articleTitle);
            mCollapsingToolbarLayout.setTitle(articleTitle);
            Date publishedDate = parsePublishedDate();
            if (!publishedDate.before(START_OF_EPOCH.getTime())) {
                bylineView.setText(Html.fromHtml(
                        DateUtils.getRelativeTimeSpanString(
                                publishedDate.getTime(),
                                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_ALL).toString()
                                + " by "
                                + mAuthor));

            } else {
                // If date is before 1902, just show the string
                bylineView.setText(Html.fromHtml(
                        outputFormat.format(publishedDate) + " by "
                                + mAuthor));

            }
            bodyView.setText(Html.fromHtml(mBody.replaceAll("(\r\n|\n)", "<br />")));
            ImageLoaderHelper.getInstance(getActivity()).getImageLoader()
                    .get(mPhotoUrl, new ImageLoader.ImageListener() {
                        @Override
                        public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                            Bitmap bitmap = imageContainer.getBitmap();
                            if (bitmap != null) {
                                mPhotoView.setImageBitmap(bitmap);
                            }
                        }

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }

                    });
        } else {
            mRootView.setVisibility(View.GONE);
            titleView.setText("N/A");
            bylineView.setText("N/A");
            bodyView.setText("N/A");
        }
    }
}
