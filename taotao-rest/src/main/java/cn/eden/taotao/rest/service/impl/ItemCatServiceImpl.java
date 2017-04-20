package cn.eden.taotao.rest.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.eden.taotao.mapper.TbItemCatMapper;
import cn.eden.taotao.pojo.TbItemCat;
import cn.eden.taotao.pojo.TbItemCatExample;
import cn.eden.taotao.pojo.TbItemCatExample.Criteria;
import cn.eden.taotao.rest.dao.JedisClient;
import cn.eden.taotao.rest.pojo.CatNode;
import cn.eden.taotao.rest.pojo.CatResult;
import cn.eden.taotao.rest.service.ItemCatService;
import cn.eden.taotao.util.JsonUtils;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${INDEX_CATEGORY_REDIS_KEY}")
	private String INDEX_CATEGORY_REDIS_KEY;

	@Override
	public CatResult getItemCatList() {
		CatResult catResult = new CatResult();
		try {
			// 添加redis业务
			// 1.先在redis中查询 ，如果有值，直接返回
			String resultString = jedisClient.get(INDEX_CATEGORY_REDIS_KEY);
			if (!StringUtils.isBlank(resultString)) {
				catResult.setData(JsonUtils.jsonToList(resultString,
						CatNode.class));
				return catResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		// 2. 如果redis没有值，查询mysql
		// 查询分类列表
		catResult.setData(getCatList(0));
		return catResult;
	}

	/**
	 * 递归查询分类列表
	 * 
	 * @param parentId
	 * @return list
	 */
	private List<?> getCatList(long parentId) {
		// 创建查询条件
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		// 执行查询
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		// 返回值listResult
		List listResult = new ArrayList();
		// 向listResult中添加节点,包装listResult
		for (TbItemCat itemCat : list) {
			// 判断是否为父节点
			if (itemCat.getIsParent()) {
				// 创建catNode并包装赋值
				CatNode catNode = new CatNode();
				catNode.setUrl("/products/" + itemCat.getId() + ".html");
				// 递归
				catNode.setItem(getCatList(itemCat.getId()));
				// 如果是根节点
				if (parentId == 0) {
					catNode.setName("<a href='/products/" + itemCat.getId()
							+ ".html'>" + itemCat.getName() + "</a>");
				} else {
					catNode.setName(itemCat.getName());
				}
				listResult.add(catNode);
			} else { // 如果是叶子节点
				listResult.add("/products/" + itemCat.getId() + ".html|"
						+ itemCat.getName());
			}
		}
		// 3. 查询完mysql后，将查询结果存入到redis中
		jedisClient.set(INDEX_CATEGORY_REDIS_KEY,
				JsonUtils.objectToJson(listResult));

		return listResult;
	}

}
