<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Регистрация</title>
</head>
<body>
  <h2>Регистрация</h2>
  <form method="post" action="<%=request.getContextPath()%>/register">
    <label>Логин:</label><br>
    <input type="text" name="login"><br>
    <label>Пароль:</label><br>
    <input type="password" name="password"><br>
    <label>Email:</label><br>
    <input type="email" name="email"><br>
    <button type="submit">Создать</button>
  </form>

  <!-- Ошибка, если была -->
  <div style="color:red">
    <%= request.getAttribute("error") == null ? "" : request.getAttribute("error") %>
  </div>

  <p>Уже есть аккаунт? <a href="<%=request.getContextPath()%>/login">Войти</a></p>
</body>
</html>