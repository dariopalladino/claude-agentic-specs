import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.callback.Callback
import com.auth0.android.result.Credentials
import com.auth0.android.authentication.AuthenticationException

class TokenManager(
    private val account: Auth0,
    private val storage: SecureStorage
) {

    fun refreshToken(onResult: (Boolean) -> Unit) {
        val refreshToken = storage.getRefreshToken()

        if (refreshToken == null) {
            onResult(false)
            return
        }

        AuthenticationAPIClient(account)
            .renewAuth(refreshToken)
            .start(object : Callback<Credentials, AuthenticationException> {

                override fun onSuccess(credentials: Credentials) {
                    // 🔥 CRITICAL: overwrite both tokens
                    storage.save(credentials)
                    onResult(true)
                }

                override fun onFailure(error: AuthenticationException) {
                    storage.clear()
                    onResult(false)
                }
            })
    }
}