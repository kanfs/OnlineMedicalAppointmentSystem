# -*- coding: utf-8 -*-
import scrapy
import re
import copy
import json
from haodf.items import HaodfItem
from scrapy import Request

# 地区列表
provs = [
    'beijing','shanghai','guangdong',
    'guangxi','jiangsu','zhejiang',
    'anhui','jiangxi','fujian',
    'shandong','sx','hebei',
    'henan','tianjin','liaoning',
    'heilongjiang','jilin','hubei',
    'hunan','sichuan','chongqing',
    'shanxi','gansu','yunnan',
    'xinjiang','neimenggu','hainan',
    'guizhou','qinghai',
    'ningxia','xizang'
]
provs_num = [
    11,12,13,14,15,21,22,23,31,32,33,34,35,36,37,41,42,43,44,45,46,50,51,52,53,54,61,62,63,64,65
]

class HaodfSpiderSpider(scrapy.Spider):
    name = 'haodf_spider'
    allowed_domains = ['haodf.com']
    start_urls = ['https://www.haodf.com/']

    def parse(self, response):
        url = "https://www.haodf.com/hospital/list-51.html" # 四川省医院
        yield Request(url, callback=self.parseProvince)
        # url = "https://www.haodf.com/hospital/list-11.html"
        # yield Request(url, callback=self.parseProvince)

    ########################爬取全国各省市的医院################################
    # def parseProvince(self, response):
    #     provinces = response.xpath("//div[@id='el_tree_1000000']//div")
    #     for province in provinces:
    #         pro = province.xpath("./a/text()").extract()
    #         if pro:
    #             item = HaodfItem()
    #             item['province'] = pro[0]
    #             href = province.xpath("./a/@href")[0].extract()
    #             province_href = "https:" + href
    #             yield Request(province_href, meta={'province': copy.deepcopy(item)}, callback=self.parseMainPage, dont_filter=True)

    # def parseMainPage(self, response):
    #     item = response.meta['province']
    #     hosp_divs = response.xpath("//div[@class='m_ctt_green']")
    #     city_divs = response.xpath("//div[@class='m_title_green']/text()").extract()
    #     i = 0
    #     for district_hosp in hosp_divs:
    #         city_name = city_divs[i]
    #         # if "其他地区" not in city_name:
    #         #     city_name = city_name + "区"
    #         i = i + 1
    #         name = district_hosp.xpath("./ul/li/a/text()").extract()
    #         href = district_hosp.xpath("./ul/li/a/@href").extract()
    #         for x in range(0, len(href)):
    #             #item = HaodfItem()
    #             item['city'] = city_name
    #             item['hospital_name'] = name[x]
    #             hosp_url = href[x]
    #             item['hospital_id'] = int(re.findall("\d+", hosp_url)[0])
    #             yield Request(hosp_url, meta={'main_page': copy.deepcopy(item)}, callback=self.parseHospital, dont_filter=True)
    
    # def parseHospital(self, response):
    #     item = response.meta['main_page']
    #     hospitalTitle = response.xpath("//span[@class='hospital-label-item']/text()").extract()
    #     # name = hospTitle.xpath("./h1/text()")[0]
    #     otherinfo = len(hospitalTitle)
    #     if(otherinfo == 3):
    #         style = hospitalTitle[0]
    #         level = hospitalTitle[1]
    #         hosptype = hospitalTitle[2]
    #     elif(otherinfo == 2):
    #         style = ""
    #         level = hospitalTitle[0]
    #         hosptype = hospitalTitle[1]
    #     elif(otherinfo == 1):
    #         style = ""
    #         level = ""
    #         hosptype = hospitalTitle[0]
    #     else:
    #         style = ""
    #         level = ""
    #         hosptype = ""
    #     item['hospital_style'] = style
    #     item['hospital_level'] = level
    #     item['hospital_type'] = hosptype

    #     t = response.xpath("//div[@class='hos-intro']/a/text()").extract()
    #     item['hospital_intro'] = t[0] if t else "暂无医院介绍信息"
    #     t = response.xpath("//div[@class='hos-phone']/text()").extract()
    #     item['telephone'] = t[0] if t else "暂无医院电话信息"
    #     t = response.xpath("//div[@class='hos-address']/text()").extract()
    #     item['hospital_address'] = t[0] if t else "暂无医院地址信息"

    #     departmentInfo = response.xpath("//span[@class='black']/text()").extract()
    #     depNum = re.findall("\d+",departmentInfo[0])[0]
    #     docNum = re.findall("\d+",departmentInfo[1])[0]
    #     item['department_num'] = int(depNum)
    #     item['doctor_num'] = int(docNum)
    
    #     yield item

    ################################爬取成都市医院的所有科室信息############################
    # def parseProvince(self, response):
    #     provinces = response.xpath("//div[@id='el_tree_1000000']//div")
    #     for province in provinces:
    #         pro = province.xpath("./a/text()").extract()
    #         if pro:
    #             href = province.xpath("./a/@href")[0].extract()
    #             province_href = "https:" + href
    #             yield Request(province_href, callback=self.parseMainPage, dont_filter=True)

    # def parseMainPage(self, response):
    #     hosp_divs = response.xpath("//div[@class='m_ctt_green']")
    #     for district_hosp in hosp_divs:
    #         href = district_hosp.xpath("./ul/li/a/@href").extract()
    #         for x in range(0, len(href)):
    #             item = HaodfItem()
    #             item['hospital_id'] = int(re.findall("\d+", href[x])[0])
    #             department_list_url = "https://www.haodf.com/hospital/"+str(item['hospital_id'])+"/keshi/list.html"
    #             yield Request(department_list_url, meta={'main_page': copy.deepcopy(item)}, callback=self.parseDepartment, dont_filter=True)

    # def parseDepartment(self, response):
    #     item = response.meta['main_page']
    #     href = response.xpath("//a[@class='faculty-item']/@href").extract()
    #     keshi = response.xpath("//div[@class='name-txt']/text()").extract()
    #     count = response.xpath("//div[@class='count']/text()").extract() # 科室医生数量 
    #     for i in range(len(href)):       
    #         item['department_id'] = int(re.findall("\d+", href[i])[1])
    #         item['department_name'] = keshi[i]
    #         item['doctor_num'] = int(re.findall("\d+", count[i])[0])
    #         yield item


    ################################爬取四川省医院的所有科室的所有医生信息############################
    def parseProvince(self, response):
        provinces = response.xpath("//div[@id='el_tree_1000000']//div")
        for province in provinces:
            pro = province.xpath("./a/text()").extract()
            if pro:
                href = province.xpath("./a/@href")[0].extract()
                province_href = "https:" + href
                yield Request(province_href, callback=self.parseMainPage, dont_filter=True)
    
    def parseMainPage(self, response):
        hosp_divs = response.xpath("//div[@class='m_ctt_green']")
        for district_hosp in hosp_divs: # 第一个对应成都市
            href = district_hosp.xpath("./ul/li/a/@href").extract()
            for x in range(0, len(href)):
                hospital_id = re.findall("\d+", href[x])[0]
                department_list_url = "https://www.haodf.com/hospital/"+str(hospital_id)+"/keshi/list.html"
                yield Request(department_list_url, callback=self.parseDepartment, dont_filter=True)
    
    def parseDepartment(self, response):
        href = response.xpath("//a[@class='faculty-item']/@href").extract()
        count = response.xpath("//div[@class='count']/text()").extract() # 科室医生数量
        
        for i in range(len(href)):
            item = HaodfItem()
            item['department_id'] = int(re.findall("\d+", href[i])[1])
            doc_num = int(re.findall("\d+", count[i])[0])
            for j in range((doc_num//20)+1):
                url = href[i].replace(".html", "/tuijian.html?type=keshi&p=%d"%(j+1))
                yield Request(url, meta={'department': copy.deepcopy(item)}, callback=self.parseDoctorList, dont_filter=True)

    def parseDoctorList(self, response):
        item = response.meta['department']
        doc_list = response.xpath("//ul[@class='doc-list']/li[@class='item']/a")
        for doc_info in doc_list:
            url = doc_info.xpath("./@href").extract()[0]
            item['doctor_id'] = int(re.findall("\d+", url)[0])
            t = doc_info.xpath("./div/p/span[@class='name']/text()").extract()
            item['doctor_name'] = t[0] if t else "无名氏"
            # print("----------------------------------------")
            # print("len(t) = " + str(len(t)) )
            # print("----------------------------------------")
            t = doc_info.xpath("./div/p/span[@class='grade']/text()").extract()
            item['doctor_grade'] = t[0] if t else "暂无"
            t = doc_info.xpath("./div/p/span[@class='edu-grade']/text()").extract()
            item['doctor_title'] = t[0] if t else "暂无"
            t = doc_info.xpath("./div/span/span[@class='score']/text()").extract()
            item['doctor_score'] = float(t[0]) if t else 3.5
            t = doc_info.xpath("./div/p[@class='goodat']/text()").extract()
            item['doctor_goodat'] = t[0] if t else "未知"
            item['doctor_intro'] = None
            item['doctor_available_time'] = None
            item['doctor_password'] = None
            # yield Request(url, meta={'doctor_list': copy.deepcopy(item)}, callback=self.parseDoctor, dont_filter=True)
            yield item

    # def parseDoctor(self, response):
    #     item = response.meta['doctor_list']
    #     t = response.xpath("//p[@class='init-content']/text()").extract()
    #     item['doctor_intro'] = None
    #     item['doctor_available_time'] = None
    #     item['doctor_password'] = None
    #     yield item

# <li class="item" data-v-713cee0b>
    # <a href="https://www.haodf.com/doctor/187998.html" target="_blank" rel="" class="item-bd" data-v-713cee0b>
        # <div class="img-wrap" data-v-713cee0b>
            # <img src="https://n1.hdfimg.com/g2/M03/71/DC/yIYBAFw8OIyAQbw2AAAWC2_R7lQ743_200_200_1.png?8901" alt="王迎春" class="avatar" data-v-713cee0b>
        # </div>
        # <div class="info" data-v-713cee0b>
            # <p class="name-grade" data-v-713cee0b>
                # <span class="name" data-v-713cee0b>王迎春</span>
                # <span class="grade" data-v-713cee0b>主任医师</span>
            # </p>
            # <p class="hos-faculty" data-v-713cee0b>湘潭市第二人民医院  心血管内科</p>
            # <p class="goodat" data-v-713cee0b>擅长：心血管内科的危急重症、疑难病例的诊治，特别是对顽固性心衰、高血压，心肾综合征等疾病的诊治</p>
            # <div class="ref" data-v-713cee0b>
                # <span class="r-title" data-v-713cee0b>专业方向：</span>
                # <span class="r-tip" data-v-713cee0b>心血管内科</span>
            # </div>
                # <div class="other-info" data-v-713cee0b>
                    # <div class="percent" data-v-713cee0b>
                        # <span class="percent-item" data-v-713cee0b>主观疗效：暂无统计</span>
                        # <span class="percent-item" data-v-713cee0b>态度：暂无统计</span>
                    # </div>
                # <div class="service" data-v-713cee0b>
                    # <span class="service-item" data-v-713cee0b>在线问诊：
                        # <span class="p-i-grey" data-v-713cee0b>未开通</span>
                    # </span>
                    # <span class="service-item" data-v-713cee0b>预约挂号：
                        # <span class="p-i-grey" data-v-713cee0b>未开通</span>
                    # </span>
                # </div>
            # </div>
        # </div>
        # <div class="tuijian-redu" data-v-713cee0b>
            # <span class="txt" data-v-713cee0b>病友推荐度</span>
            # <span class="score-wrap" data-v-713cee0b>
                # <span class="score" style="display:;" data-v-713cee0b>3.0</span>
                # <span class="score-nodata" style="display:none;" data-v-713cee0b>暂无</span>
                # <span class="label" style="display:none;" data-v-713cee0b></span>
            # </span>
        # </div>
        # <div class="check-detail" data-v-713cee0b>
            # <span class="check-btn" data-v-713cee0b>查看详情</span>
        # </div>
    # </a>
# </li>
            
# https://www.haodf.com/nhospital/pc/keshi/menzhen/getMenzhenList?hospitalFacultyId=5321&date=2023-12-23
# {"errorCode":0,
#  "data":
#  [{"time":"上午",
#    "scheduleList":[{"scheduleId":9350517086,"batchId":9231095435,"doctorId":139765,"status":3,"sourceId":9230842203,"sourceType":"period","scheduleFacultyId":139765,"scheduleDate":"2023-12-23","weekDay":6,"period":1,"hospitalCampusId":5642429913,"hospitalFacultyId":5321,"site":"","scheduleSourceType":"专科","scheduleSourceTypeDesc":"","explain":"","fee":0,"amount":0,
#                     "doctorInfo":{"doctorName":"席静","doctorGrade":"主治医师","doctorEducateGrade":"讲师","doctorIsShowAlias":0,"hospitalFacultyName":"神经内科","hosName":"华西医院"}},
#                     {"scheduleId":9350505585,"batchId":9229602841,"doctorId":25065,"status":3,"sourceId":9229615399,"sourceType":"period","scheduleFacultyId":25065,"scheduleDate":"2023-12-23","weekDay":6,"period":1,"hospitalCampusId":5642429913,"hospitalFacultyId":5321,"site":"","scheduleSourceType":"专家","scheduleSourceTypeDesc":"","explain":"{\"scheduleSpecialHint\":\"\",\"schedulePatientType\":\"\",\"scheduleAstrict\":\"\"}","fee":0,"amount":0,
#                      "doctorInfo":{"doctorName":"任安文","doctorGrade":"","doctorEducateGrade":"教授","doctorIsShowAlias":0,"hospitalFacultyName":"神经内科","hosName":"华西医院"}},
#                      {"scheduleId":9350505589,"batchId":9229602924,"doctorId":25151,"status":3,"sourceId":9230654966,"sourceType":"period","scheduleFacultyId":25151,"scheduleDate":"2023-12-23","weekDay":6,"period":1,"hospitalCampusId":5642429913,"hospitalFacultyId":5321,"site":"第二门诊部","scheduleSourceType":"专家","scheduleSourceTypeDesc":"","explain":"","fee":0,"amount":0,
#                      "doctorInfo":{"doctorName":"蒋毅","doctorGrade":"","doctorEducateGrade":"副教授","doctorIsShowAlias":0,"hospitalFacultyName":"神经内科","hosName":"华西医院"}}]},{"time":"中午","scheduleList":[]},{"time":"下午","scheduleList":[{"scheduleId":9350505590,"batchId":9229602924,"doctorId":25151,"status":3,"sourceId":9230666090,"sourceType":"period","scheduleFacultyId":25151,"scheduleDate":"2023-12-23","weekDay":6,"period":2,"hospitalCampusId":5642429913,"hospitalFacultyId":5321,"site":"第二门诊部","scheduleSourceType":"专家","scheduleSourceTypeDesc":"","explain":"","fee":0,"amount":0,
#                                                                                                                                                                                                                                       "doctorInfo":{"doctorName":"蒋毅","doctorGrade":"","doctorEducateGrade":"副教授","doctorIsShowAlias":0,"hospitalFacultyName":"神经内科","hosName":"华西医院"}}]},{"time":"晚上","scheduleList":[]}]}