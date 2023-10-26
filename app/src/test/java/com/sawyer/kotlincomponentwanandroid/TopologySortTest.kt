package com.sawyer.kotlincomponentwanandroid

import com.sawyer.component.sort.TopologySort
import com.sawyer.component.startup.Startup
import com.sawyer.component.startup.StartupSortStore
import com.sawyer.component.task.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TopologySortTest {

    @Test
    fun testTopologySort(){
        val list = mutableListOf<Startup<*>>().apply {
            add(Task1())
            add(Task3())
            add(Task2())
            add(Task5())
            add(Task4())
        }
        val sort: StartupSortStore = TopologySort.sort(list)
        val result = sort.result
        val builder = StringBuilder()
        builder.apply {
            append("====================排序结果====================\n")
            append("Task Graph:\n")
            for (startup in result){
                append("          ")
                append(startup.javaClass.name)
                append("\n")
            }
            append("===============================================\n")
        }
        print("$builder")
    }

}