package com.imooc.mgallery.dao;

import com.imooc.mgallery.entity.Painting;
import com.imooc.mgallery.utils.PageModel;
import com.imooc.mgallery.utils.XmlDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * 油画数据访问对象
 */
public class PaintingDao {
    /**
     * 分页查询油画数据
     * @param page 页号
     * @param rows 每页记录数
     * @return PageModel分页对象
     */
    public PageModel pagination(int page,int rows){
        //Painting油画对象集合
        List<Painting> list = XmlDataSource.getRawData();
        PageModel pageModel = new PageModel(list, page, rows);
        return pageModel;
    }
    public PageModel pagination(int category,int page,int rows){
        List<Painting> list = XmlDataSource.getRawData();
        List<Painting> categoryList=new ArrayList<>();
        for(Painting p:list){
            if(p.getCategory()==category){
                categoryList.add(p);
            }
        }
        PageModel pageModel = new PageModel(categoryList, page, rows);
        return pageModel;
    }
    /**
     * 数据新增
     */
    public void create(Painting painting){
        XmlDataSource.append(painting);
    }

    public Painting findById(Integer id){
        List<Painting> data = XmlDataSource.getRawData();
        Painting painting=null;
        for(Painting p:data){
            if(p.getId()==id){
                painting= p;
                break;
            }
        }
        return painting;
    }

    /**
     * 数据更新
     * @param painting
     */
    public void update(Painting painting) {
        XmlDataSource.update(painting);
    }

    /**
     * 数据删除
     * @param id
     */
    public void delete(Integer id) {
        XmlDataSource.delete(id);
    }
}
