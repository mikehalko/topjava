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
        <title>Create new Meal</title>
    </head>

    <div>
        <form action="/topjava/meals/new" method="POST" >
            <fieldset>

                <legend>Create new Meal</legend>

                <div>
                    <label for="date">Date:
                        <input type="datetime-local" id="date" name="date">
                    </label>
                </div>
                <div>
                    <label for="desc">Description:
                        <input type="text" id="desc" name="description">
                    </label>
                </div>
                <div>
                    <label for="cal">Calories:
                        <input type="number" id="cal" name="calories">
                    </label>
                </div>

                <section>
                    <p>
                        <button type="submit">create</button>
                    </p>
                </section>

            </fieldset>
        </form>
    </div>

    <form name="index_meals" action="/topjava/meals" method="get">
        <input type="submit" value="back">
    </form>
</html>
