<%@ page import="ru.javawebinar.topjava.model.MealTo" %><%--
  Created by IntelliJ IDEA.
  User: Пользователь
  Date: 15.10.2022
  Time: 20:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

    <head>
        <title>Meal</title>
    </head>

    <body>
        <%
        MealTo meal = (MealTo) request.getAttribute("meal");

        if (meal != null) {
            String  trueStr = "ПРЕВЫШЕНО!";
            String falseStr = "всё в порядке :)";

            String date = meal.getDateTime().toString();
            String desc = meal.getDescription();
            String cal  = String.valueOf(meal.getCalories());
            String excess = meal.isExcess() ? trueStr : falseStr;
            String id = String.valueOf(meal.getId());

            String line =
                    "<p>Дата: "+ date +
                    "\n</p><p>Описание: "+ desc +
                    "\n</p><li>Кол-во калорий: "+ cal +
                    "\n</li><li>Превышение калорий: "+ excess +
                    "\n<div></li><a href=\"meals/edit?id="+ id +"\">edit</a><br>" +
                    "<hr><a href=\"meals/delete?id="+ id +"\">delete</a></div>";

            out.print(line);
        } else {
            out.print("Не найден!");
        }
    %>
    </body>

    <form name="index_meals" action="/topjava/meals" method="get">
        <input type="submit" value="back">
    </form>
</html>
