package com.wakaw.darkmode

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.gilbecker.ButtonState
import com.gilbecker.DarkThemeButton
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.setupWithNavController(findNavController(R.id.nav_host_fragment))

        // add listener
        // override handler
        darkThemeButton.moonColor = darkThemeButton.sunColor


//        darkThemeButton.setButtonChangeHandler(object : DarkThemeButton.StateChangedHandler {
//            override fun onInflate(currentState: ButtonState) {
//                Toast.makeText(applicationContext, "Inflated", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onAnimationEnded(newState: ButtonState) {
//                Toast.makeText(applicationContext, "Switched to $newState", Toast.LENGTH_SHORT).show()
//            }
//        })
    }
}