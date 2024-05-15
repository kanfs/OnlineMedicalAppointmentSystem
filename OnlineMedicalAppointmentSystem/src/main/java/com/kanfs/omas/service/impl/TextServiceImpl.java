package com.kanfs.omas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanfs.omas.pojo.Text;
import com.kanfs.omas.service.TextService;
import com.kanfs.omas.mapper.TextMapper;
import org.springframework.stereotype.Service;

/**
* @author 方盛
* @description 针对表【text】的数据库操作Service实现
*/
@Service
public class TextServiceImpl extends ServiceImpl<TextMapper, Text>
    implements TextService{

}




