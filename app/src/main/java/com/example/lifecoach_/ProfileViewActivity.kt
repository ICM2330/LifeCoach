import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lifecoach_.ChatMenuActivity
import com.example.lifecoach_.DashBoardHabitsActivity
import com.example.lifecoach_.FriendActivity
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.ActivityFriendBinding
import com.example.lifecoach_.databinding.ActivityProfileViewBinding

class ProfileViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        manageButtons(binding)
    }

    fun manageButtons(binding: ActivityProfileViewBinding){
        bottomNavigationBarManagement()
    }

    fun bottomNavigationBarManagement(){
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuProfile-> {
                    //Do an intent with the profile activity
                    val intent = Intent(baseContext, ProfileViewActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menuChat -> {
                    // Do an intent with the chat activity
                    val intent = Intent(baseContext, ChatMenuActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menuHabits->{
                    // Do an intent with the dashboard of habits activity
                    val intent = Intent(baseContext, DashBoardHabitsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menuFriends->{
                    // Do an intent with the friends activity
                    val intent = Intent(baseContext, FriendActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

}
