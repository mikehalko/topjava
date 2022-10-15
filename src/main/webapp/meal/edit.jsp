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

            String input = "<div>\n" +
                    "        <form action=\"/topjava/meals/edit?id="+id+"\" method=\"POST\" >\n" +
                    "            <fieldset>\n" +
                    "\n" +
                    "                <legend>Edit this Meal</legend>\n" +
                    "\n" +
                    "                <div>\n" +
                    "                    <label for=\"date\">Date:\n" +
                    "                        <input type=\"datetime-local\" id=\"date\" name=\"date\" value=\""+date+"\">\n" +
                    "                    </label>\n" +
                    "                </div>\n" +
                    "                <div>\n" +
                    "                    <label for=\"desc\">Description:\n" +
                    "                        <input type=\"text\" id=\"desc\" name=\"description\" value=\""+desc+"\">\n" +
                    "                    </label>\n" +
                    "                </div>\n" +
                    "                <div>\n" +
                    "                    <label for=\"cal\">Calories:\n" +
                    "                        <input type=\"number\" id=\"cal\" name=\"calories\" value=\""+cal+"\">\n" +
                    "                    </label>\n" +
                    "                </div>\n" +
                    "\n" +
                    "                <section>\n" +
                    "                    <p>\n" +
                    "                        <button type=\"submit\">edit</button>\n" +
                    "                    </p>\n" +
                    "                </section>\n" +
                    "\n" +
                    "            </fieldset>\n" +
                    "        </form>\n" +
                    "    </div>";

            out.print(input);
        } else {
            out.print("Не найден!");
        }
    %>
    </body>

    <form name="index_meals" action="/topjava/meals" method="get">
        <input type="submit" value="back">
    </form>
</html>
