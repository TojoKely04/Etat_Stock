<%-- 
    Document   : Etat_Stock
    Created on : 24 janv. 2023, 20:58:29
    Author     : TOJOKELY
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="stock.EtatStock"%>
<%
    EtatStock[] listEtatStock = (EtatStock[])request.getAttribute("etatStock"); 
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Etat De Stock</title>
    </head>
    <body>
        <p>Etat de stock : <%= request.getAttribute("etat") %></p>
        <table border="1">
            <tr>
                <th>Produit</th>
                <th>Entree</th>
                <th>Sortie</th>
                <th>Reste</th>
                <th>Valeur</th>
            </tr>
            <% for(int i=0 ; i<listEtatStock.length ;i++) { %>
                <tr>
                    <td><%= listEtatStock[i].getProduit().getNom() %></td>
                    <td><%= listEtatStock[i].getEntree() %></td>
                    <td><%= listEtatStock[i].getSortie() %></td>
                    <td><%= listEtatStock[i].getReste() %></td>
                    <td><%= listEtatStock[i].getValeur() %></td>
                </tr>
            <% } %>
        </table>
    </body>
</html>
