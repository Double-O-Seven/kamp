package ch.leadrian.samp.kamp.annotations

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class CallbackListener(val runtimePackageName: String = "", val apiPackageName: String = "")
