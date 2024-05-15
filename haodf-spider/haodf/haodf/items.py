# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# https://doc.scrapy.org/en/latest/topics/items.html

import scrapy

class HaodfItem(scrapy.Item):
    #hospital.sql
    # hospital_id = scrapy.Field()
    # province = scrapy.Field()
    # city = scrapy.Field()
    # hospital_name = scrapy.Field()
    # hospital_style = scrapy.Field()
    # hospital_level = scrapy.Field()
    # hospital_type = scrapy.Field()
    # hospital_address = scrapy.Field()
    # department_num = scrapy.Field()
    # doctor_num = scrapy.Field()
    # telephone = scrapy.Field()
    # hospital_intro = scrapy.Field()

    #department.sql
    # hospital_id = scrapy.Field()
    # department_id = scrapy.Field()
    # department_name = scrapy.Field()
    # doctor_num = scrapy.Field()

    #doctor.sql
    department_id = scrapy.Field()
    doctor_name = scrapy.Field()
    doctor_id = scrapy.Field()
    doctor_password = scrapy.Field()
    doctor_grade = scrapy.Field()
    doctor_title = scrapy.Field()
    doctor_score = scrapy.Field()
    doctor_intro = scrapy.Field()
    doctor_goodat = scrapy.Field()
    doctor_available_time = scrapy.Field()
