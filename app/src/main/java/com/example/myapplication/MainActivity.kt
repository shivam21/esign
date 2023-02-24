package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myapplication.databinding.ActivityWebviewBinding

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityWebviewBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		binding = DataBindingUtil.setContentView(this, R.layout.activity_webview)
		super.onCreate(savedInstanceState)
//		val url =
//			Uri.parse("https://esign2.signzy.tech/success?signedFile=https:%2F%2Fesign-persist.signzy.tech%2Fapi%2Ffiles%2F499161589%2Fdownload%2Ff1985b5c82a344159f93a3d081e96b85352e598458e7499a9dc4dd3795b0b25c.pdf&transactionId=UKC:eSign:3881:20230216124646220&timestamp=2023-02-16T12:47:48&status=1&redirectTime=10&redirectUrl=com.aims:%2F%2Fesign")
//		url.queryParameterNames.forEach {
//			Log.d("TAG1221", "onCreate: key:${it} value:${url.getQueryParameter(it)}")
//		}

		getWebViewWithInitialSetup()
		val url = "https://esign2.signzy.tech/nsdl-esign-customer2/633570abae75a456b02fdba1/token/vmUoYAhKuvjHxdAywf08q0fnDiPkC1tyERpQFUF9UFo4fJqAbiXr4ejJfI7F1676547839008"
		binding.webView.loadUrl(url)
	}

	fun getWebViewWithInitialSetup() =
		binding.webView.apply {
			webViewClient = MyWebViewClient()
			enableWebViewDebugging()
			enableWebViewSettings()
			keepScreenOn = true
			setLayerType(View.LAYER_TYPE_HARDWARE, null)
		}

	@SuppressLint("SetJavaScriptEnabled")
	protected fun WebView.enableWebViewSettings() {
		settings.apply {
			loadsImagesAutomatically = true
			useWideViewPort = true
			allowContentAccess = true
			allowFileAccess = true
			databaseEnabled = true
			domStorageEnabled = true
			javaScriptEnabled = true
			layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
			loadWithOverviewMode = true
		}
	}

	inner class MyWebViewClient : WebViewClient() {
		override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
			super.onPageStarted(view, url, favicon)
			onPageStartedInWebView()
		}

		override fun onPageFinished(view: WebView?, url: String?) {
			onPageFinishedInWebView(url)
			super.onPageFinished(view, url)
		}
	}

	private fun enableWebViewDebugging() {
		WebView.setWebContentsDebuggingEnabled(true)
	}

	fun onPageStartedInWebView() {
		binding.progressBar.visibility = View.VISIBLE
	}

	fun onPageFinishedInWebView(url: String?) {
		url?.let {
			if (checkIfUrlIsRedirectUrl(it)) {
				Log.d("TAG1221", "onPageFinishedInWebView: $url")
				Toast.makeText(this, "Redirect uri $url detected", Toast.LENGTH_SHORT).show()
				startActivity(Intent(this, SuccessScreen::class.java))
				binding.webView.visibility = View.GONE
			} else binding.progressBar.visibility = View.GONE
		}
	}

	private fun checkIfUrlIsRedirectUrl(it: String): Boolean {
		return it.contains("com.aims")
	}
}