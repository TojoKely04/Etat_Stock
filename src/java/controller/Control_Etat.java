/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import bdd.Connect;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import objet.Magasin;
import stock.EtatStock;

/**
 *
 * @author TOJOKELY
 */
public class Control_Etat extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Control_Etat</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Control_Etat at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter p = response.getWriter();
        Connection connection;
        String etat = request.getParameter("etat");
        EtatStock[] listEtatStock = null;
        try {
            connection = Connect.seConnecterOracle("stock","stock");
            ArrayList listById = (new Magasin()).selectById(connection, "magasin", request.getParameter("id_magasin"));
            Magasin magasin = (Magasin)listById.get(0);
            magasin.setProduits(connection);
            
//            p.println(etat);
            
            if(etat.equals("nonDate")){
                magasin.setEtatStockNonDate(connection);
            }
            
            

            
            if(etat.equals("date")){
                Date date = null;
                if(request.getParameter("date")=="") {
                    date = Date.valueOf(LocalDate.now());
                }else{
                    date = Date.valueOf(request.getParameter("date"));
                }
                magasin.setEtatStockDate(connection , date );
            }
            if(etat.equals("optimise")){
                Date date = null;
                if(request.getParameter("date")=="") {
                    date = Date.valueOf(LocalDate.now());
                }else{
                    date = Date.valueOf(request.getParameter("date"));
                }
                magasin.setEtatStockOptimise(connection ,  date);
            }
            
            listEtatStock=magasin.getEtatStock();
            connection.close();
        } catch (Exception ex) {
            p.println(ex.getMessage());
        }        
        request.setAttribute("etatStock", listEtatStock);
        request.setAttribute("etat", etat);

        RequestDispatcher dispat =  request.getRequestDispatcher("Etat_Stock.jsp");
        dispat.forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    
}
