package com.imooc.mgallery.utils;

import com.imooc.mgallery.entity.Painting;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据源类，用于将XML文件解析为java对象
 */
public class XmlDataSource {
    //通过static静态关键字保证数据全局唯一
    private static List<Painting> data=new ArrayList<>();
    private static String dataFile;
    static{
        //通过XmlDataSource类路径的到painting.xml文件路径
        dataFile = XmlDataSource.class.getResource("/painting.xml").getPath();
        System.out.println(dataFile);
        reload();
    }
    private static void reload(){
        //对得到的路径进行征程格式转换
        URLDecoder decoder = new URLDecoder();
        try {
            dataFile=decoder.decode(dataFile,"UTF-8");
//            System.out.println(dataFile);
            //利用Don4j对xml进行解析
            SAXReader reader = new SAXReader();
            //1.获取Document文档对象
            Document document = reader.read(dataFile);
            //2.Xpath得到xml节点集合
            List<Node> nodes = document.selectNodes("/root/painting");
            data.clear();
            for (Node node : nodes) {
                Element element = (Element) node;
                String id = element.attributeValue("id");
                String pname = element.elementText("pname");
                System.out.println(id+":"+pname);
                Painting painting = new Painting();
                painting.setId(Integer.parseInt(id));
                painting.setPname(pname);
                painting.setCategory(Integer.parseInt(element.elementText("category")));
                painting.setPrice(Integer.parseInt(element.elementText("price")));
                painting.setPreview(element.elementText("preview"));
                painting.setDescription(element.elementText("description"));
                System.out.println(element.elementText("category"));
                System.out.println(element.elementText("preview"));
                data.add(painting);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取所有油画Painting对象
     * @return Painting List
     */
    public static List<Painting> getRawData(){
        return data;
    }

    public static void append(Painting painting){
        System.out.println(dataFile);
        //1.读取XML文档，得到Document对象
        SAXReader reader = new SAXReader();
        Writer writer = null;
        try {
            Document document = reader.read(dataFile);
            //2.创建新的painting节点
            Element root = document.getRootElement();//得到根节点 <root>
            Element p = root.addElement("painting");//创建节点 <painting>
            //3.创建painting节点的各个子节点
            p.addAttribute("id",String.valueOf(data.size()));//设置painting节点的id属性
            p.addElement("pname").setText(painting.getPname());
            p.addElement("category").setText(String.valueOf(painting.getCategory()));
            p.addElement("price").setText(String.valueOf(painting.getPrice()));
            p.addElement("preview").setText(painting.getPreview());
            p.addElement("description").setText(painting.getDescription());
            //4.写入XML，完成追加操作
            System.out.println(dataFile);
            writer=new OutputStreamWriter(new FileOutputStream(dataFile),"UTF-8");
            document.write(writer);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(writer!=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            reload();//内存与文件的数据一致
        }

    }

    /**
     * 更新对应id的XML油画数据
     * @param painting 要更新的油画数据
     * @throws IOException
     */
    public static void update(Painting painting) {
        SAXReader reader = new SAXReader();
        Writer writer = null;
        try {
            Document document=reader.read(dataFile);
            //节点路径[@属性名=属性值]
            // /root/painting[@id=x]
            List<Node> nodes = document.selectNodes("/root/painting[@id=" + painting.getId() + "]");
            if(nodes.size() == 0) {
                throw new RuntimeException("id=" + painting.getId() + "编号油画不存在");
            }
            Element p = (Element)nodes.get(0);
            p.selectSingleNode("pname").setText(painting.getPname());
            p.selectSingleNode("category").setText(painting.getCategory().toString());
            p.selectSingleNode("price").setText(painting.getPrice().toString());
            p.selectSingleNode("preview").setText(painting.getPreview());
            p.selectSingleNode("description").setText(painting.getDescription());
            writer = new OutputStreamWriter(new FileOutputStream(dataFile),"UTF-8");
            document.write(writer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            reload();
        }

    }

    /**
     * 按id号删除XML油画数据
     * @param id 油画id
     * @throws IOException
     */

    public static void delete(Integer id) {
        SAXReader reader = new SAXReader();
        Writer writer = null;
        try {
            Document document = reader.read(dataFile);
            List<Node> nodes = document.selectNodes("/root/painting[@id=" + id + "]");
            if(nodes.size() == 0) {
                throw new RuntimeException("id=" + id + "编号油画不存在");
            }
            Element p = (Element)nodes.get(0);
            p.getParent().remove(p);
            writer = new OutputStreamWriter(new FileOutputStream(dataFile),"UTF-8");
            document.write(writer);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(writer!=null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            reload();
        }
    }

//    public static void main(String[] args) {
//        Painting painting = new Painting();
//        painting.setId(16);
//        painting.setPname("aa");
//        painting.setCategory(1);
//        painting.setPrice(5000);
//        painting.setPreview("/upload/1111.jpg");
//        painting.setDescription("....");
//        new XmlDataSource().append(painting);
//
//    }
}
