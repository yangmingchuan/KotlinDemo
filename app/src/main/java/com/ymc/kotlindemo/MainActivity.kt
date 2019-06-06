package com.ymc.kotlindemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

typealias NumPrint = (Int) -> Int
typealias Num2Print = (Int) -> Unit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(R.id.tv_text) {
            tv_text.text = "ymc"
            tv_text.textSize = 23f
        }

        R.id.tv_text.run {
            tv_text.text = "ymc"
            tv_text.textSize = 23f
        }
    }



    fun main(args: Array<String>) {

        fun List<Int>.initList(double: NumPrint): List<Int> {
            var resultList  = arrayListOf<Int>()

            // 循环 将集合item 都带入lambda 函数中
            for(item in this){
                resultList.add(double(item))
            }
            return resultList
        }

        val oddNum:  Num2Print = {
            if (it % 2 == 1) {
                println(it)
            } else {
                println("is not a odd num")
            }
        }

        val evenNum: Num2Print = {
            if (it % 2 == 0) {
                println(it)
            } else {
                println("is not a even num")
            }
        }

        oddNum.invoke(100)
        evenNum.invoke(100)


        var oneList = arrayListOf<Int>(1,2,3)

        // 默认调用List 的tostring 方法
        println(oneList)
        // 调用并定义 lambda 函数
        var twoList = oneList.initList { it * 2 }
        println(twoList)
    }


}
