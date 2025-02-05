package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var newsViewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()
    lateinit var binding: FragmentArticleBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)

        newsViewModel = (activity as NewsActivity).newsViewModel
        val article = args.article

        if (article != null && article.url.isNotEmpty()) {
            Log.d("ArticleFragment", "Article URL: ${article.url}")
            binding.webView.apply {
                webViewClient = object : WebViewClient() {
                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        Snackbar.make(view ?: return, "Error loading page", Snackbar.LENGTH_SHORT).show()
                    }
                }
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.setSupportZoom(true)  // Optional: Allow zooming in WebView
                loadUrl(article.url)
            }
        } else {
            Snackbar.make(view, "Invalid article data", Snackbar.LENGTH_SHORT).show()
        }

        binding.fab.setOnClickListener {
            if (article != null) {
                newsViewModel.addToFavorite(article)
                Snackbar.make(view, "Added to favorites", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(view, "Article data not available", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
