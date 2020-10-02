package com.sklagat46.proelswear.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.sklagat46.proelswear.R
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        btn_intro_sign_in.setOnClickListener{
            startActivity(Intent(this, SignInActivity::class.java))
        }

        btn_intro_sign_up.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }


    }


}