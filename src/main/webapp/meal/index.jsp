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
<html>
<head>
    <title>List of Meals</title>
</head>
<body>

<body>
    <table border="1" cellpadding="4" style="border-style: wave">
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

                    String line = "<tr>" +
                            "<td>"+ date +"</td>" +
                            "<td>"+ desc +"</td>" +
                            "<td>"+ cal +"</td>" +
                            "<td>"+ excess +"</td>" +
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
</body>

<form name="home" action="/topjava/meals/new" method="get">
    <input type="submit" value="create">
</form>

<form name="home" action="index.html" method="post">
    <input type="submit" value="back">
</form>
</body>
</html>
