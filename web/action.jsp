<%-- 
    Document   : action
    Created on : 25 janv. 2023, 00:33:04
    Author     : TOJOKELY
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String id_magasin = request.getParameter("id");
%>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form method="get" action="Control_Etat">
            Etat De Stock Non-Daté
            <input type="hidden" name="id_magasin" value="<%= id_magasin %>">
            <input type="hidden" name="etat" value="nonDate">
            <input type="submit" value="Voir">
        </form>
           <br>

        <form method="get" action="Control_Etat">
            Etat De Stock Daté
            <input type="hidden" name="id_magasin" value="<%= id_magasin %>">
            <input type="hidden" name="etat" value="date">
            <input type="date" name="date">
            <input type="submit" value="Voir">
        </form>
            <br>
        <form method="get" action="Control_Etat?id_magasin=<%= id_magasin %>&etat=optimiser">
            Etat De Stock Optimisé
            <input type="hidden" name="id_magasin" value="<%= id_magasin %>">
            <input type="hidden" name="etat" value="optimise">
            <input type="date" name="date">
            <input type="submit" value="Voir">
        </form>

    </body>
</html>
