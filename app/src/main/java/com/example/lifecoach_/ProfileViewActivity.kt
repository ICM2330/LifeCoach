import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.ActivityProfileViewBinding

class ProfileViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profileMenu-> {
                    //Use the activity of
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu -> {
                    // Ya estás en la actividad de perfil, no es necesario hacer nada
                    true
                }
                // Agrega más casos para otros items si es necesario
                else -> false
            }
        }
    }
}
