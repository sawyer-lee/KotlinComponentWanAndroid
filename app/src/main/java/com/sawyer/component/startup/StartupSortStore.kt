package com.sawyer.component.startup

class StartupSortStore(
    //存放拓扑排序后的列表
    val result: List<Startup<Any>>,
    //通过任务的class找到其实例
    val startupMap: HashMap<Class<Startup<Any>>, Startup<Any>>,
    //当前任务的子任务
    val startupChildrenMap: HashMap<Class<Startup<Any>>, MutableList<Class<Startup<Any>>>>
)