<%--
  Created by IntelliJ IDEA.
  User: Пользователь
  Date: 14.10.2022
  Time: 18:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>List of Meals</title>
</head>
<body bgcolor="lightgrey">

<table border="1" cellpadding="1">
    <colgroup>
        <col span="4">
        <col style="background: gray;" span="3">
    </colgroup>
    <tr>
        <th>Дата</th>
        <th>Описание</th>
        <th>Кол-во каллорий</th>
        <th>Превышение калорий</th>
        <th>Посмотреть</th>
        <th>Изменить</th>
        <th>Удалить</th>
    </tr>
    <%
        List<MealTo> meals = (List<MealTo>) request.getAttribute("list");

        if (meals != null && !meals.isEmpty()) {
            String  trueStr = "!!!";
            String falseStr = "x";

            for (MealTo meal : meals) {
                String date = meal.getDateTime().toString();
                String desc = meal.getDescription();
                String cal  = String.valueOf(meal.getCalories());
                String excess = meal.isExcess() ? trueStr : falseStr;
                String id = String.valueOf(meal.getId());
                String styleExcess = meal.isExcess() ?  " style=\"background: palevioletred;\"" : "";

                String line = "<tr align=\"center\">" +
                        "<td"+styleExcess+">"+ date +"</td>" +
                        "<td"+styleExcess+">"+ desc +"</td>" +
                        "<td"+styleExcess+">"+ cal +"</td>" +
                        "<td"+styleExcess+">"+ excess +"</td>" +
                        "<td><a href=\"meals/show?id="+ id +"\">show</a></td>" +
                        "<td><a href=\"meals/edit?id="+ id +"\">edit</a></td>" +
                        "<td><a href=\"meals/delete?id="+ id +"\">delete</a></td>" +
                        "</tr>\n";

                out.print(line);
            }
        } else {
            out.print("Список пуст!");
        }
    %>
</table>
<hr>
<nav>
    <form name="home" action="/topjava/meals/new" method="get">
        <input type="submit" value="add new">
    </form>
    <br>
    <form name="home" action="index.html" method="post">
        <input type="submit" value="back">
    </form>
</nav>
</body>
</html>
