# -//软件工程何敉华）探究实现本地文件与S3文件的同步问题，目前大致思路为：使用aws sdk对本地文件目录监视，
通过比较在S3中列出的文件列表，与本地文件的列表两者差异进行。
创建一个S3中的对比列表，遍历列表中的所有文件，当文件不同于本地目录时，进行删除或者修改等；
因为文件内容采用Etag标识符，当Etag不同时，可以将本地文件修改后的部分，上传S3
（class2）

实现创建目标桶，生成一个桶文件列表，和本地文件列表，添加和删除文件命令，分块上传；
所遇难点，如何比较本地目录和bucket目录文件差异
小组内利用org.apache.commons.io监控文件变化
（class3)

（5.26）完善小组作业，学习实训操作，利用spark对文件进行分词计算
通过SSH进入spark账号，执行SQL操作，使用数据表计算，Hive登录与计算

（5.27）创建外部表，学习Grennplum数据聚合，在IDEA中进行Module的创建，编辑Demo类
复习巩固Spark-SQL命令。

(5.28)温习数据湖实训操作，完成课后扩展1，2

(5.29)完成课后扩展3

(6.1)学习实操五、六，新建工具类Utils，读取图片并进行处理
中途遇到困难，后发现spark使用过程中环境没有配置齐全

(6.2)练习实操，实操二过程中出现乱码等问题，kafka重置后问题解决

(6.3)建Model类，构造函数接收Controller对象

(6.5)继续实现离线计算大作业的Model类，连接sparksql,导出所有表名+字段名。

(6.8)上传离线大作业等文件夹，学习大数据实时操作的剩下实操，了解流式协同处理

(6.9)学习kafka架构和原理，理解kafka集群功能

(6.10）复习大数据流式计算实操，做扩展题5

(6.11)做实时流式计算实践作业

(6.12)大数据离线计算作业，上传文件，项目链接：https://github.com/hemihua159/dataflow_opt_hw.git

(6.15)上传大数据流式计算实操文件，学习机器学习进行模型训练，调超参数
使用pipeline进行模型训练

(6.16)学习数据治理，创建业务模型、模型设计，连接数据库，元数据抓取

(6.17)做人工智能大作业

(6.18)做人工智能大作业

(6.22)上周错过时间，大数据作业无法连接操作环境，现运行后文件打包，提交工程，链接： https://github.com/hemihua159/test2.git
学习数据治理实操任务
