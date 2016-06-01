<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: pizmak
  Date: 2016-04-07
  Time: 10:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Orders</title>

        <style>
            table#allOrders th,tr {
            background-color: #f1f1c1;
            border: 1px solid black;
            }
        </style>
    </head>
    <body>
        List of all orders :

        <table id="allOrders">
            <thead>
                <tr>
                    <th>id</th>
                    <th>type</th>
                    <th>quantity</th>
                </tr>
            </thead>

            <c:forEach items="${listOfAllOrders}" var="order">
                <tr>
                    <td><c:out value="${order.id}" /></td>
                    <td><c:out value="${order.type}"/></td>
                    <td><c:out value="${order.quantity}" /></td>
                </tr>
            </c:forEach>
        </table>


    </body>
</html>
