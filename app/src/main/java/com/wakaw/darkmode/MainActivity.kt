package com.wakaw.darkmode

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gilbecker.DarkThemeButton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(findViewById(R.id.toolbar))


//		darkThemeButton.setSwitchThemeOnClick(false)
		darkThemeButton.setOnThemeChangedListener { isDark ->
			val theme = if (isDark) "night" else "morning"
			Toast.makeText(this@MainActivity, "Good $theme!", Toast.LENGTH_SHORT).show()
//			finish()
//			startActivity(Intent(this, javaClass))
//			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
		}
//
//		darkThemeButton.setMoonColor(Color.parseColor("#000000"))
//		darkThemeButton.setSunColor(Color.RED)
	}

//	override fun onCreateOptionsMenu(menu: Menu): Boolean {
//		val menuInflater = menuInflater
//		menuInflater.inflate(R.menu.menu_main, menu)x
////		val getItem: MenuItem = menu.findItem(R.id.get_item)
////		if (getItem != null) {
////			val button = getItem.getActionView() as AppCompatButton
////			//Set a ClickListener, the text,
////			//the background color or something like that
////		}
//		return super.onCreateOptionsMenu(menu)
//	}
}