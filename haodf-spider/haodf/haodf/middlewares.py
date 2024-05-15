# -*- coding: utf-8 -*-

# Define here the models for your spider middleware
#
# See documentation in:
# https://doc.scrapy.org/en/latest/topics/spider-middleware.html
import random

from scrapy import signals
from scrapy.downloadermiddlewares.httpcompression import HttpCompressionMiddleware
import json
import requests
import logging

class HaodfSpiderMiddleware(object):
    # Not all methods need to be defined. If a method is not defined,
    # scrapy acts as if the spider middleware does not modify the
    # passed objects.

    @classmethod
    def from_crawler(cls, crawler):
        # This method is used by Scrapy to create your spiders.
        s = cls()
        crawler.signals.connect(s.spider_opened, signal=signals.spider_opened)
        return s

    def process_spider_input(self, response, spider):
        # Called for each response that goes through the spider
        # middleware and into the spider.

        # Should return None or raise an exception.
        return None

    def process_spider_output(self, response, result, spider):
        # Called with the results returned from the Spider, after
        # it has processed the response.

        # Must return an iterable of Request, dict or Item objects.
        for i in result:
            yield i

    def process_spider_exception(self, response, exception, spider):
        # Called when a spider or process_spider_input() method
        # (from other spider middleware) raises an exception.

        # Should return either None or an iterable of Response, dict
        # or Item objects.
        pass

    def process_start_requests(self, start_requests, spider):
        # Called with the start requests of the spider, and works
        # similarly to the process_spider_output() method, except
        # that it doesn’t have a response associated.

        # Must return only requests (not items).
        for r in start_requests:
            yield r

    def spider_opened(self, spider):
        spider.logger.info('Spider opened: %s' % spider.name)


class HaodfDownloaderMiddleware(object):
    # Not all methods need to be defined. If a method is not defined,
    # scrapy acts as if the downloader middleware does not modify the
    # passed objects.

    @classmethod
    def from_crawler(cls, crawler):
        # This method is used by Scrapy to create your spiders.
        s = cls()
        crawler.signals.connect(s.spider_opened, signal=signals.spider_opened)
        return s

    def process_request(self, request, spider):
        # Called for each request that goes through the downloader
        # middleware.

        # Must either:
        # - return None: continue processing this request
        # - or return a Response object
        # - or return a Request object
        # - or raise IgnoreRequest: process_exception() methods of
        #   installed downloader middleware will be called
        # request.cookies = {'g': '15797_1700810332521', 
        #                    'krandom_a119fcaa84': '759614', 
        #                    'HMF_CI': '46e3c802d244007734e1810136747e449bf180ba27cb5773f740f648a4309236e666aae80fea469932d90f8de9b35273571d0c8ea9b12c939d135d02ce226e5e6f', 
        #                    'Hm_lvt_dfa5478034171cc641b1639b2a5b717d': '1700810333', 
        #                    'Hm_lpvt_dfa5478034171cc641b1639b2a5b717d': '1700810487'
        #                    'userinfo[id]': '10735407907', 
        #                    'userinfo[time]': '1700810475', 
        #                    'userinfo[hostid]': '0', 
        #                    'userinfo[key]': 'USwFNFRmB2ABaAAyBjAAYl82UmcAMFA0BSxSPVNhDGcCNQRpADJTO1pkB3hTNQIjX30BPVZ%%2F', 
        #                    'userinfo[ver]': '1.0.2', 
        #                    'userinfo[name]': 'hdfbm08dxds', 
        #                    }
        # 在发送请求之前调用
        
        return None

    def process_response(self, request, response, spider):
        # Called with the response returned from the downloader.

        # Must either;
        # - return a Response object
        # - return a Request object
        # - or raise IgnoreRequest
        return response

    def process_exception(self, request, exception, spider):
        # Called when a download handler or a process_request()
        # (from other downloader middleware) raises an exception.

        # Must either:
        # - return None: continue processing this exception
        # - return a Response object: stops process_exception() chain
        # - return a Request object: stops process_exception() chain
        pass

    def spider_opened(self, spider):
        spider.logger.info('Spider opened: %s' % spider.name)



class RandomUserAgentMiddleware(object):
    def __init__(self, user_agent):
        self.user_agent = user_agent
        self.request_count = 0
        self.max_requests = 100  # 设置触发暂停的请求次数

    @classmethod
    def from_crawler(cls, crawler):
        # 从settings中加载USER_AGENT的值
        return cls(
            user_agent=crawler.settings.getlist('USER_AGENT')  # getlist()就是返回list类型
        )

    def process_request(self, request, spider):
        # 在process_request中设置User-Agent的值
        # self.request_count += 1
        # if self.request_count > self.max_requests:
        #     spider.logger.info("Reached maximum requests count. Pausing spider.")
        #     spider.crawler.engine.close_spider(spider, "Reached maximum requests count")
        request.headers.setdefault('User-Agent', random.choice(self.user_agent))
