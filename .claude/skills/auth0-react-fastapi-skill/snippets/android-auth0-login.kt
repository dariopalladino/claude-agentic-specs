class LoginActivity : AppCompatActivity() {

    private lateinit var account: Auth0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        account = Auth0(
            "YOUR_CLIENT_ID",
            "YOUR_DOMAIN"
        )
    }

    fun login() {
        WebAuthProvider.login(account)
            .withScheme("your.app.id")
            .withAudience("YOUR_API_AUDIENCE")
            .start(this, object : Callback<Credentials, AuthenticationException> {
                override fun onSuccess(credentials: Credentials) {
                    // Save tokens securely
                }

                override fun onFailure(error: AuthenticationException) {
                    Log.e("Auth0", error.getDescription())
                }
            })
    }
}