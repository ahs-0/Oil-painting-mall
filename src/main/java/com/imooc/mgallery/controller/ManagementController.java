package com.imooc.mgallery.controller;

import com.imooc.mgallery.entity.Painting;
import com.imooc.mgallery.service.PaintingService;
import com.imooc.mgallery.utils.PageModel;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

@WebServlet("/management")
public class ManagementController extends HttpServlet {
    private PaintingService paintingService=new PaintingService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        String method = request.getParameter("method");
        if(method.equals("list")){
            this.list(request,response);
        }else if(method.equals("delete")){
            this.delete(request,response);
        }else if(method.equals("show_create")){
            this.showCreatePage(request,response);
        }else if(method.equals("create")){
            this.create(request,response);
        }else if(method.equals("show_update")){
            this.showUpdatePage(request,response);
        }else if(method.equals("update")) {
            this.update(request,response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page = request.getParameter("p");
        String rows = request.getParameter("r");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="6";
        }
        PageModel pageModel = paintingService.pagination(Integer.parseInt(page), Integer.parseInt(rows));
        request.setAttribute("pageModel",pageModel);
        request.getRequestDispatcher("/list.jsp").forward(request,response);
    }
    //??????????????????
    private void showCreatePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/create.jsp").forward(request,response);
    }
    //????????????
    private void create(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.?????????FileUpload??????
        FileItemFactory factory=new DiskFileItemFactory();
        /**
         * FileItemFactory ????????????????????????????????????????????????FileItem??????
         * ServletFileUpload ?????????FileUpload????????????java web???Http????????????
         */
        ServletFileUpload sf = new ServletFileUpload(factory);
        //2.????????????FileItem
        try {
            List<FileItem> formData = sf.parseRequest(request);
            Painting painting = new Painting();
            for (FileItem fi : formData) {
                if(fi.isFormField()){
//                    System.out.println("??????????????????"+fi.getFieldName()+"");
                    switch(fi.getFieldName()){
                        case "pname":
                            painting.setPname(fi.getString("UTF-8"));
                            break;
                        case "category":
                            painting.setCategory(Integer.parseInt(fi.getString("UTF-8")));
                            break;
                        case "price":
                            painting.setPrice(Integer.parseInt(fi.getString("UTF-8")));
                            break;
                        case "description":
                            painting.setDescription(fi.getString("UTF-8"));
                            break;
                    }
                }else{
                    System.out.println("???????????????"+fi.getFieldName());
                    //3.??????????????????????????????
                    String path = request.getServletContext().getRealPath("/upload");
                    //???????????????
                    String fileName = UUID.randomUUID().toString();
                    //fi.getName()??????????????????????????????????????????.??????????????????
                    String suffix = fi.getName().substring(fi.getName().lastIndexOf("."));
                    fi.write(new File(path,fileName+suffix));
                    painting.setPreview("upload/"+fileName+suffix);
                }

            }
            paintingService.create(painting);//????????????
            response.sendRedirect("/mgallery_war_exploded/management?method=list");//???????????????
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ??????????????????
     * @param request ????????????
     * @param response ????????????
     * @throws ServletException ??????
     * @throws IOException ??????
     */
    private void showUpdatePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        Painting painting = paintingService.findById(Integer.parseInt(id));
        request.setAttribute("painting",painting);
        request.getRequestDispatcher("/update.jsp").forward(request,response);
    }

    /**
     * ??????????????????
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void update(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        int isPreviewModified = 0;
        //?????????????????????????????????????????????????????????
		/*
		String pname = request.getParameter("pname");
		System.out.println(pname);
		*/
        //1. ?????????FileUpload??????
        FileItemFactory factory = new DiskFileItemFactory();
        /**
         * FileItemFactory ????????????????????????????????????????????????FileItem??????
         * ServletFileUpload ?????????FileUpload????????????Java web???Http????????????
         */
        ServletFileUpload sf = new ServletFileUpload(factory);
        //2. ????????????FileItem
        try {
            List<FileItem> formData = sf.parseRequest(request);
            Painting painting = new Painting();
            for(FileItem fi:formData) {
                if(fi.isFormField()) {
                    System.out.println("???????????????:" + fi.getFieldName() + ":" + fi.getString("UTF-8"));
                    switch (fi.getFieldName()) {
                        case "pname":
                            painting.setPname(fi.getString("UTF-8"));
                            break;
                        case "category":
                            painting.setCategory(Integer.parseInt(fi.getString("UTF-8")));
                            break;
                        case "price":
                            painting.setPrice(Integer.parseInt(fi.getString("UTF-8")));
                            break;
                        case "description":
                            painting.setDescription(fi.getString("UTF-8"));
                            break;
                        case "id":
                            painting.setId(Integer.parseInt(fi.getString("UTF-8")));
                            break;
                        case "isPreviewModified":
                            isPreviewModified = Integer.parseInt(fi.getString("UTF-8"));
                            break;
                        default:
                            break;
                    }
                }else {
                    if(isPreviewModified == 1) {
                        System.out.println("???????????????:" + fi.getFieldName());
                        //3.??????????????????????????????
                        String path = request.getServletContext().getRealPath("/upload");
                        System.out.println("??????????????????:" + path);
                        //String fileName = "test.jpg";
                        String fileName = UUID.randomUUID().toString();
                        //fi.getName()?????????????????????,??????????????????.??????????????????,??????:wxml.jpg->.jpg
                        String suffix = fi.getName().substring(fi.getName().lastIndexOf("."));
                        //fi.write()??????????????????
                        fi.write(new File(path,fileName + suffix));
                        painting.setPreview("upload/" + fileName + suffix);
                    }
                }
            }
            //???????????????????????????
            paintingService.update(painting, isPreviewModified);
            response.sendRedirect("/mgallery_war_exploded//management?method=list");//???????????????
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * ???????????????Ajax????????????Http??????
     * Controller?????????????????????????????????jsp,????????????????????????JSON???????????????
     * Tips:??????Ajax?????????????????????,?????????????????????HTML,?????????????????????????????????
     * @throws IOException
     */

    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        PrintWriter out = response.getWriter();
        try {
            paintingService.delete(Integer.parseInt(id));
            //{"result":"ok"}
            out.println("{\"result\":\"ok\"}");
        }catch(Exception e) {
            e.printStackTrace();
            out.println("{\"result\":\"" + e.getMessage() + "\"}");
        }
    }
}
