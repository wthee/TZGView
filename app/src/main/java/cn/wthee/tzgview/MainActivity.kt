package cn.wthee.tzgview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tzg = findViewById<TianZiGeTextView>(R.id.tzg)
        tzg.setWord("测试一下")
        tzg.setLineSize(100f)
        tzg.setWordSize(50f)
        tzg.updateView()
    }
}
