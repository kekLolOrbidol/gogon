package com.vushnevskiy.gogon.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.vushnevskiy.gogon.logger.EventLogger
import javax.inject.Inject

class AnalyticsEventLogger @Inject constructor(private val firebaseAnalytics: FirebaseAnalytics) : EventLogger {

  override fun logEvent(name: String, params: Map<String, String>?) {

    firebaseAnalytics.logEvent(name, params?.let {
      Bundle().apply {
        params.forEach { putString(it.key, it.value) }
      }
    })
  }

}