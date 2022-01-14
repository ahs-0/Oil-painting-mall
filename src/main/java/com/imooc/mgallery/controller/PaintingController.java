package com.imooc.mgallery.controller;

import com.imooc.mgallery.service.PaintingService;
import com.imooc.mgallery.utils.PageModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/page")
public class PaintingController extends HttpServlet {
    private PaintingService paintingService=new PaintingService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page = request.getParameter("p");
        String rows = request.getParameter("r");
        String category = request.getParameter("c");
        if(page==null){
            page="1";
        }
        if(rows==null){
            rows="6";
        }
        PageModel pageModel = paintingService.pagination(Integer.parseInt(page), Integer.parseInt(rows),category);
        request.setAttribute("pageModel",pageModel);
        request.getRequestDispatcher("/page.jsp").forward(request,response);
    }
}
