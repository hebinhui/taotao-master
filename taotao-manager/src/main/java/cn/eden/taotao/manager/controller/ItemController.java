package cn.eden.taotao.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.eden.taotao.manager.pojo.DataGridResult;
import cn.eden.taotao.manager.service.ItemService;
import cn.eden.taotao.pojo.TbItem;
import cn.eden.taotao.pojo.TbItemDesc;
import cn.eden.taotao.util.TaotaoResult;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{id}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long id) {
		return itemService.getItemById(id);
	}
	
	/**
	 * 获取商品信息<br>
	 * 后台获取分页控件自动提交的两个参数rows每页显示的记录数和page当前第几页
	 * @param page 当前第几页
	 * @param rows 每页显示的记录数
	 * @return 具有page、rows的pojo
	 */
	@RequestMapping("/item/list")
	@ResponseBody
	public DataGridResult getItemsByPage(long page, long rows) {
		return itemService.getItemsByPage(page, rows);
	}
	
	/**
	 * 商品添加
	 * @param item 商品信息
	 * @param desc 商品描述信息
	 * @return TaotaoResult
	 */
	@RequestMapping("/item/save")
	@ResponseBody
	public TaotaoResult addItem(TbItem item, String desc, String itemParams) {
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemDesc(desc);
		return itemService.addItem(item, itemDesc, itemParams); 
	}
	
	/**
	 * 加载商品描述
	 * @param id
	 * @return TbItemDesc
	 */
	@RequestMapping("/rest/item/query/item/desc/{id}")
	@ResponseBody
	public TbItemDesc listItemDesc(@PathVariable Long id) {
		return itemService.listItemDesc(id);
	}
	
	/**
	 * 更新商品和商品描述
	 * @param item
	 * @param desc
	 * @return TaotaoResult
	 */
	@RequestMapping("/rest/item/update")
	@ResponseBody
	public TaotaoResult updateItem(TbItem item, String desc) {
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemDesc(desc);
		return itemService.updateItem(item, itemDesc);
	}
	
	/**
	 * 商品删除
	 * @param ids
	 * @return TaotaoResult
	 */
	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public TaotaoResult deleteItem(String ids) {
		return itemService.deleteItem(ids);
	}
}
