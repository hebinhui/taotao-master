package cn.eden.taotao.portal.service;

import cn.eden.taotao.pojo.TbItem;

public interface ItemService {
	TbItem getItemById(Long itemId);
	String getItemDescById(Long itemId);
	String getItemParamItemById(Long itemId);
}
