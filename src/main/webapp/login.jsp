<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Вход</title>
  <style>
    body { font-family: sans-serif; max-width: 400px; margin: 50px auto; }
    input, button { padding: 8px; width: 100%; margin: 6px 0; }
    .err { color: red; }
  </style>
</head>
<body>
  <h2>Вход</h2>
  <form method="post" action="<%=request.getContextPath()%>/login">
    <label>Логин:</label>
    <input type="text" name="login" required>
    <label>Пароль:</label>
    <input type="password" name="password" required>
    <button type="submit">Войти</button>
  </form>

  <!-- Блок для ошибки -->
  <div class="err">
    <%= request.getAttribute("error") == null ? "" : request.getAttribute("error") %>
  </div>

  <p>Нет аккаунта? <a href="<%=request.getContextPath()%>/register">Регистрация</a></p>
</body>
</html>