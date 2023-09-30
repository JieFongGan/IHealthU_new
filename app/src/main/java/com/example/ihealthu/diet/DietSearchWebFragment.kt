package com.example.ihealthu.diet

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.example.ihealthu.databinding.FragmentDietSearchWebBinding

class DietSearchWebFragment : Fragment() {
    private var _binding: FragmentDietSearchWebBinding? = null
    private val binding get() = _binding!!
    private lateinit var dsWebView:WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentDietSearchWebBinding.inflate(inflater, container, false)
        dsWebView = binding.dsWebView
        val searchQuery = arguments?.getString("searchQuery") ?: "DefaultSearchText"
        webViewSetup(searchQuery)
        return binding.root
    }
@SuppressLint("SetJavaScriptEnabled")
private fun webViewSetup(search:String){
    dsWebView.webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view?.loadUrl(url.toString())
            return true
        }
    }
    dsWebView.apply{
        loadUrl("https://www.google.com/search?q=Diet+Plan+$search")
        settings.javaScriptEnabled = true
    }
}

}