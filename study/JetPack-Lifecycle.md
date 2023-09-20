## Lifecycle 生命周期感知

观察者设计模式
观察者-被观察者

Activity - Fragment 实现了 LifecycleOwner

SavedStateRegistryController 单例 管理 LifecycleOwner 

Activity 

Fragment

定义 生命周期到达事件为 State 
Initiated Created Started Resumed Destroyed
定义 生命周期事件 为 Event
OnCreate OnStart OnResume OnPause OnStop OnDestroy