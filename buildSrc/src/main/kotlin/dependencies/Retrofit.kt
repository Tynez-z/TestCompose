package dependencies

object Retrofit {

    /**
     * [Converter Retrofit]
     * https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-gson
     */
    const val converterGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"

    /**
     * [Retrofit 2]
     * https://mvnrepository.com/artifact/com.squareup.retrofit2/retrofit
     */
    const val retrofit2 = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"

    /**
     * [Interceptor OkHttp]
     * https://mvnrepository.com/artifact/com.squareup.okhttp3/logging-interceptor
     */
    const val interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.interceptor}"

    /**
     * [Gson ]
     * https://mvnrepository.com/artifact/com.google.code.gson/gson
     */
    const val gson = "com.google.code.gson:gson:${Versions.gson}"

    /**
     * [OkHttp ]
     * https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
     */
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.interceptor}"
}