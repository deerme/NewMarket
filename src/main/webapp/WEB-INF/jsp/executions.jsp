<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: pizmak
  Date: 2016-04-07
  Time: 10:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Executions</title>

    <style>
        table#allExecutions th,tr {
            background-color: #f1f1c1;
            border: 1px solid black;
        }
    </style>
</head>
    <body>
    List of all executions:
        <table id="allExecutions">
            <thead>
                <tr>
                    <th>id</th>
                    <th>idBuyer</th>
                    <th>idSeller</th>
                    <th>quantity</th>
                </tr>
            </thead>

            <c:forEach items="${listOfAllExecutions}" var="execution">
                <tr>
                    <td><c:out value="${execution.id}"/></td>
                    <td><c:out value="${execution.idBuyer}"/></td>
                    <td><c:out value="${execution.idSeller}"/></td>
                    <td><c:out value="${execution.quantity}"/></td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
