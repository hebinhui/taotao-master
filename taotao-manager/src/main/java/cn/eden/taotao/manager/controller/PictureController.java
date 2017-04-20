package cn.eden.taotao.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.eden.taotao.manager.pojo.PictureResult;
import cn.eden.taotao.manager.service.PictureService;
@Controller
public class PictureController {
	@Autowired
	private PictureService pictureService;
	
	@RequestMapping("/pic/upload")
	@ResponseBody
	public PictureResult upload(MultipartFile uploadFile) {
		return pictureService.uploadPicture(uploadFile);
	}
}
