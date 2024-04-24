package miso4203.mobile.app.vinilos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import miso4203.mobile.app.vinilos.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVisitorLogin.setOnClickListener {
            Toast.makeText(this, "Welcome Visitor", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("is_collector", false)
            startActivity(intent)
        }

        binding.btnCollectorLogin.setOnClickListener {
            Toast.makeText(this, "Welcome Collector", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("is_collector", true)
            startActivity(intent)
        }
    }
}