      ## Java 基础
面向对象语言

    三大特性: 封装, 继承, 多态

基本类型
    
    int
    short
    long
    float
    double
    byte
    char
    bool

类
    
    // 类定义
    class Name{
        // 属性定义
        String name;
        public void setName(String n){
            this.name = n;
        }
        public String getName(){
            return this.name;    
        }
        // 构造方法
        public Name(){}
    }

集合

    Map
    
    List

    Set


反射
    
    在运行状态中,对于任意一个类, 都能够知道这个类的所有属性和方法;
    对于任何一个对象,都能够调用它的任意一个方法和属性;
    这种动态获取的信息以及动态调用对象的方法的功能称为 Java 语言的反射机制.

注解

