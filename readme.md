UESTC 本科毕业设计 kanfs

***\*实现成果：\****

一个有四类角色（系统管理员、医院管理员、医生、用户）的在线医疗预约平台，支持预约挂号、医学咨询、智能分诊、智能问医、排班管理等功能。

***\*使用到的主要工具版本：\****

Java版本：JDK 17.0.8

Python版本：3.6.8

前台npm版本：20.10.0

后台npm版本：12.14.0

数据库：mysql 8.0.29

知识图谱：neo4j 1.5.9

Java开发工具：IntelliJ IDEA 2022.2.3

***\*使用的数据集：\****

医疗知识图谱数据集

https://github.com/liuhuanyong/QASystemOnMedicalKG

智能分诊模型数据集

https://huggingface.co/datasets/FreedomIntelligence/Huatuo26M-Lite

意图判断模型数据集

https://github.com/ishine/CMID

实体识别模型数据集

https://github.com/alibaba-research/ChineseBLUE

***\*使用到的Bert预训练模型：\****

实体识别模型：哈工大讯飞实验室开发的BERT-wwm, Chinese模型https://github.com/ymcui/Chinese-BERT-wwm

意图识别模型、智能分诊模型：哈工大讯飞实验室开发的RBT3, Chinese模型https://github.com/ymcui/Chinese-BERT-wwm

***\*运行方法：\****

1. 下载所需的数据集和bert预训练模型放在所在的位置
2. 解压数据库文件并使用数据库运行.sql文件
3. 运行xxxxx-spider模块爬取医院。科室、医生数据信息
4. 启动neo4j，运行./OnlineMedicalAppointmentSystem\src\main\resources\python\KBQA-for-Diagnosis\build_knowledge_graph\build_medicalgraph.py文件构建医疗知识图谱
5. 运行OnlineMedicalAppointmentSystem\src\main\resources\python下的BertTriage、KBQA-for-Diagnosis\knowledge_extraction\bert_bilstm_crf和KBQA-for-Diagnosis\nlu\bert_intent_recognition下的train.py文件训练模型
6. 使用IDEA打开后端项目OnlineMedicalAppointmentSystem和xxl_job分别运行
7. 打开前端项目前台模块mas-front和mas-admin先(注意npm的版本)npm install再运行对应的.bat文件
8. 用户端为http://locolhost:8080 管理员端：http://locolhost:9518 定时任务管理端：http://localhost:8899

 

 
