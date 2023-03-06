<%-- 
    Document   : index.jsp
    Created on : 25 janv. 2023, 00:20:58
    Author     : TOJOKELY
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="objet.Magasin"%>
<%@page import="java.util.ArrayList"%>

<%
    Magasin[] magasins = Magasin.allMagasin();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <% for(int i=0 ; i<magasins.length ;i++) { %>
            <a href="action.jsp?id=<%= magasins[i].getId_magasin() %>"> <%= magasins[i].getNom() %> </a>
            <br>
        <% } %>
    </body>
</html>
