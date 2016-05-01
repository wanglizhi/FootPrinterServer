## Footprinter 服务器端

![](https://raw.githubusercontent.com/wanglizhi/FootPrinterServer/master/logo.png)

Footprinter项目简介见[Footprinter Client](https://github.com/wanglizhi/FootPrinterClient)

这部分当时**都是**由我负责，RMI接口调用（接口和PO定义在FootprinterRemote）是团队开会讨论的，在服务器端就是负责数据PO的增删改查，此外还用HttpClient写了个爬虫，根据citys.txt里提供的城市连接，到去哪网爬去城市和景点的数据，主要包括简介、图片、营业时间、标签、酒店、购物中心信息等等。

#### 技术

主要是SQL的增删改查，以及相应的Unit Test，代码**没能做好设计、复用**，多写了很多SQL。

爬虫都是有规则的HTML页面，特定的爬取目标，没有JS干扰，设置了sleep防止被封IP。

![](https://raw.githubusercontent.com/wanglizhi/FootPrinterServer/master/images/滨湖公园.jpg)

**说明** 为减小体积，代码中的lib包和一些资源文件有删除