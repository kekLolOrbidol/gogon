package com.vushnevskiy.gogon.logger

interface EventLogger {
  fun logEvent(name:String, params:Map<String, String>? = null)
}
