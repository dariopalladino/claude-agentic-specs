import android.app.Activity
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.lifecycle.ViewModel
import com.auth0.android.Auth0
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.callback.Callback
import com.auth0.android.result.Credentials
import com.auth0.android.authentication.AuthenticationException

sealed class AuthState {
    object Loading : AuthState()
    object Unauthenticated : AuthState()
    object Authenticated : AuthState()
}

class AuthViewModel(
    private val account: Auth0,
    private val storage: SecureStorage
) : ViewModel() {

    var state by mutableStateOf<AuthState>(AuthState.Unauthenticated)
        private set

    fun login(activity: Activity) {
        WebAuthProvider.login(account)
            .withScheme("your.app.id")
            .withAudience("YOUR_API_AUDIENCE")
            .start(activity, object : Callback<Credentials, AuthenticationException> {

                override fun onSuccess(credentials: Credentials) {
                    storage.save(credentials)
                    state = AuthState.Authenticated
                }

                override fun onFailure(error: AuthenticationException) {
                    state = AuthState.Unauthenticated
                }
            })
    }

    fun logout() {
        storage.clear()
        state = AuthState.Unauthenticated
    }
}

@Composable
fun AuthScreen(viewModel: AuthViewModel) {
    when (viewModel.state) {

        is AuthState.Loading -> {
            CircularProgressIndicator()
        }

        is AuthState.Unauthenticated -> {
            Button(onClick = {
                viewModel.login(LocalContext.current as Activity)
            }) {
                Text("Login")
            }
        }

        is AuthState.Authenticated -> {
            Column {
                Text("Logged in")
                Button(onClick = { viewModel.logout() }) {
                    Text("Logout")
                }
            }
        }
    }
}