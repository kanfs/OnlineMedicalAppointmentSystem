package com.kanfs.omas.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kanfs.omas.pojo.Text;
import com.kanfs.omas.pojo.User;
import com.kanfs.omas.result.Result;
import com.kanfs.omas.service.TextService;
import com.kanfs.omas.service.UserService;
import com.kanfs.omas.utils.AITriageUtil;
import com.kanfs.omas.utils.DozerUtil;
import com.kanfs.omas.utils.MedicalConsultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class TextController {
    @Autowired
    private TextService textService;
    @Autowired
    private UserService userService;
    @Autowired
    private DozerUtil dozerUtil;
    @Autowired
    private AITriageUtil aiTriageUtil;
    @Autowired
    private MedicalConsultUtil medicalConsultUtil;

    @GetMapping("/mas/user/list/{doctorId}")
    public Result getUsers(@PathVariable("doctorId") String doctorId) {
        // .eq(Text::getSenderType, "USER")
        List<Text> textList = textService.list(new LambdaQueryWrapper<Text>().eq(Text::getDoctorId, doctorId).orderByAsc(Text::getCreationTime));
        List<User> users = new ArrayList<>();
        for (int i = 0; i < textList.size(); i++) if(!users.contains(userService.getById(textList.get(i).getUserId()))) users.add(userService.getById(textList.get(i).getUserId()));
        return Result.ok(users);
    }

    @GetMapping("/mas/user-text/{doctorId}/{userId}")
    public Result getUserTexts(@PathVariable("doctorId") String doctorId, @PathVariable("userId") String userId) {
        // .eq(Text::getSenderType, "USER")
        List<Text> textList = textService.list(new LambdaQueryWrapper<Text>().eq(Text::getUserId, userId).eq(Text::getDoctorId, doctorId).orderByAsc(Text::getCreationTime));
        // 返回textList 将textList深拷贝得到texts 将texts中每个text的status置为true 调用textService.updateBatchById(texts);改变状态
        List<Text> texts = dozerUtil.mapList(textList, Text.class);
        for (int i = 0; i < texts.size(); i++) if(texts.get(i).getSenderType().equals("DOCTOR")) texts.get(i).setStatus(true);
        textService.updateBatchById(texts);
        return Result.ok(textList);
    }

    @GetMapping("/mas/doctor-text/{doctorId}/{userId}")
    public Result getDoctorTexts(@PathVariable("doctorId") String doctorId, @PathVariable("userId") String userId) {
        // .eq(Text::getSenderType, "USER")
        List<Text> textList = textService.list(new LambdaQueryWrapper<Text>().eq(Text::getUserId, userId).eq(Text::getDoctorId, doctorId).orderByAsc(Text::getCreationTime));
        // 返回textList 将textList深拷贝得到texts 将texts中每个text的status置为true 调用textService.updateBatchById(texts);改变状态
        List<Text> texts = dozerUtil.mapList(textList, Text.class);
        for (int i = 0; i < texts.size(); i++) if(texts.get(i).getSenderType().equals("USER")) texts.get(i).setStatus(true);
        textService.updateBatchById(texts);
        return Result.ok(textList);
    }

    @GetMapping("/mas/text-doctor")
    public Result sendDoctorText(@RequestParam Map params) {
        // .eq(Text::getSenderType, "DOCTOR")
        Text text = dozerUtil.map(params, Text.class);
        text.setStatus(false);
        text.setSenderType("USER");
        text.setCreationTime(new Date());
        return Result.ok(textService.save(text));
    }

    @GetMapping("/mas/text-user")
    public Result sendUserText(@RequestParam Map params) {
        Text text = dozerUtil.map(params, Text.class);
        text.setStatus(false);
        text.setSenderType("DOCTOR");
        text.setCreationTime(new Date());
        return Result.ok(textService.save(text));
    }

    @GetMapping("/mas/text-ai-triage")
    public Result getAITriage(@RequestParam Map params) throws IOException {
        // .eq(Text::getSenderType, "DOCTOR")
        Text text = dozerUtil.map(params, Text.class);
        text.setStatus(false);
        text.setSenderType("USER");
        text.setCreationTime(new Date());
        Text reply = Text.builder().userId(text.getUserId()).doctorId(1L).senderType("DOCTOR").status(true)
                .content(aiTriageUtil.sendMessage(text.getContent())).creationTime(new Date()).build();
        textService.save(text);
        textService.save(reply);
        return Result.ok();
    }

    @GetMapping("/mas/text-medical-consult")
    public Result getMedicalConsult(@RequestParam Map params) throws IOException, InterruptedException {
        // .eq(Text::getSenderType, "DOCTOR")
        Text text = dozerUtil.map(params, Text.class);
        text.setStatus(false);
        text.setSenderType("USER");
        text.setCreationTime(new Date());
        Text reply = Text.builder().userId(text.getUserId()).doctorId(2L).senderType("DOCTOR").status(true)
                .content(medicalConsultUtil.sendMessage(text.getContent())).creationTime(new Date()).build();
        textService.save(text);
        textService.save(reply);
        return Result.ok();
    }
}
