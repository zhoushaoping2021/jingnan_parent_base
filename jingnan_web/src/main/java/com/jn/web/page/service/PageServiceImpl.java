package com.jn.web.page.service;

import com.alibaba.fastjson.JSON;
import com.jn.pojo.Category;
import com.jn.pojo.Sku;
import com.jn.pojo.Spu;
import com.jn.web.goods.service.CategoryService;
import com.jn.web.goods.service.SkuService;
import com.jn.web.goods.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 静态页Page业务层
 */
@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private SpuService spuService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TemplateEngine templateEngine;

    //生成的静态页面的路径
    @Value("${pagepath}")
    private String path;


    @Override
    public Map<String, Object> findPageAllDataBySpuId(String spuId) {
        Map<String, Object> resultMap = new HashMap<>();

        //1. 根据spuid获取商品数据
        Spu spu = spuService.findById(spuId);

        //2. 根据spuid获取对应的sku集合数据
        List<Sku> skuList = skuService.findBySpuId(spuId);

        if (spu == null) {
            throw new RuntimeException("商品id没有对应的商品数据, 无法生成详情页, spuid====" + spuId);
        }
        if (skuList == null || skuList.size() == 0) {
            throw new RuntimeException("商品id没有对应的sku库存数据, 无法生成详情页, spuid====" + spuId);
        }

        //3. 获取分类数据
        Category category1 = categoryService.findOneById(spu.getCategory1Id());
        Category category2 = categoryService.findOneById(spu.getCategory2Id());
        Category category3 = categoryService.findOneById(spu.getCategory3Id());

        //4. 获取图片数据
        String imageJsonStr = spu.getImages();
        String imageList = "";
        if (!StringUtils.isEmpty(imageJsonStr)) {
            //[{"color":"银白色","url":"//img11.360buyimg.com/n7/jfs/t30040/100/1290632710/208879/1f7e2225/5cdd0d92Nb78895a6.jpg"}]
            List<Map> imagesList = JSON.parseArray(imageJsonStr, Map.class);
            if (imagesList != null && imagesList.size() > 0) {
                imageList = String.valueOf(imagesList.get(0).get("url"));
            }
        }


        //5. 获取规格数据
        String specItemsJsonStr = spu.getSpecItems();
        Map specificationList = JSON.parseObject(specItemsJsonStr, Map.class);


        resultMap.put("spu", spu);
        resultMap.put("skuList", skuList);
        resultMap.put("category1", category1);
        resultMap.put("category2", category2);
        resultMap.put("category3", category3);
        resultMap.put("specificationList", specificationList);
        resultMap.put("imageList", imageList);

        return resultMap;
    }

    @Override
    public void createStaticPage(Map<String, Object> dataMap, String spuId) throws Exception {
        //1. 将页面需要的数据放入模板引擎中
        Context context = new Context();
        context.setVariables(dataMap);

        //2. 定义输出流, 指定文件输出的位置
        path = path + "/" + spuId +".html";
        PrintWriter printWriter = new PrintWriter(path);

        //3. 生成,
        //第一个参数, 模板名称, 第二个参数: 模板中需要的额数据, 第三个参数: 输出流, 文件输出的位置和文件名
        templateEngine.process("item", context, printWriter);
    }
}
