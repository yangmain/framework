# 氡氪网络科技基础架构 - 开发命令工具包
本工具包主要用于完成数据库的逆向工程，从数据库中的表生成Java实体对象、数据访问对象和MyBatis的Mapper.xml文件。
## 注意
在新项目开发中不建议使用本工具包进行逆向，建议先定义实体和数据访问对象，通过正向生成类扫描实体生成建库脚本，同时在开发过程中也可青松完成字段的增减改操作。

## 使用方法
双击运行或命令行运行如下文件
```
\bin\orm-cli.cmd
```

### 进入交互界面为如下界面

```
Welcome to rnkrsoft orm toolkit!
JLine support is enabled
cli> 闪动的光标

```

### 获取帮助
输入help ,然后回车，将展示如下界面
```
Welcome to rnkrsoft orm toolkit!
JLine support is enabled
cli>help
toolkit用法:
帮助信息(help)
用法: help -options
其中选项包括:
  -c | -cmd arg
                                命令 默认值:null
help -c reverse
有关详细信息, 请参阅 http://www.rnkrsoft.com/framework/help

退出CLI(exit)

有关详细信息, 请参阅 http://www.rnkrsoft.com/framework/help

查看历史命令(history)

有关详细信息, 请参阅 http://www.rnkrsoft.com/framework/help

逆向工程(reverse)
用法: reverse -options
其中选项包括:
  -s | -schema 字符串数据库名
                                数据库模式 默认值:[]
  -o | -output .
                                输出路径 默认值:[.]
 省略。。。

```

所有支持的命有
```
help - 帮助信息
exit - 退出CLI
history - 查看历史命令
reverse - 逆向工程
```

### 逆向工程命令
用于完成输入数据库地址，端口，用户名，密码，数据库名以及生成的保存位置和包名进行逆向生成实体，数据访问对象和Mapper.xml，
生成的目录结构为Maven Java项目约定的结构如下：
```
src\
    main\
        java\
            com\xxxx\demo\
                        dao\*.java
                        entity\*.java
        resource\
                com\xxxx\demo\
                            mapper\*.xml

```

默认情况下逆向生成保存的根路径为当前路径 ,即"."，可通过 -o | -output ./xxx/来指定保存的新的根路径。

生成的Mapper.xml文件，自动根据字段的情况，生成一个默认的结果集和SQL片段（字段输出）可以作为修改模板。

如果使用过程中有什么疑问，可访问http://www.rnkrsoft.com获取更多帮助。