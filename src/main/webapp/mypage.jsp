<%@ page import="java.io.File,java.text.SimpleDateFormat,java.util.Date,java.net.URLEncoder" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Файлы</title>
  <style>
    body { font-family: sans-serif; }
    table { border-collapse: collapse; width: 100%; }
    th, td { border-bottom: 1px solid #eee; padding: 6px 8px; text-align: left; }
    th { background: #f8f8f8; }
    a { text-decoration: none; }
  </style>
</head>
<body>
  <div style="color:#555;margin:8px 0"><%= request.getAttribute("now") %></div>
  <h2 style="margin:0"><%= request.getAttribute("currentPath") %></h2>

  <%
    String parent = (String) request.getAttribute("parent");
    if (parent != null) {
  %>
    <p><a href="<%=request.getContextPath()%>/files?path=<%=parent%>">Вверх</a></p>
  <%
    }
  %>

  <table>
    <tr>
      <th>Файл</th>
      <th>Размер</th>
      <th>Дата</th>
      <th>Действия</th>
    </tr>
  <%
    File[] files = (File[]) request.getAttribute("files");
    if (files != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
      String currentPath = (String) request.getAttribute("currentPath");
      for (File f : files) {
        String childPath = currentPath + "/" + f.getName();
        String enc = URLEncoder.encode(childPath, "UTF-8");
  %>
    <tr>
      <td>
        <%
          if (f.isDirectory()) {
        %>
          <a href="<%=request.getContextPath()%>/files?path=<%= childPath %>"><%= f.getName() %>/</a>
        <%
          } else {
        %>
          <%= f.getName() %>
        <%
          }
        %>
      </td>
      <td><%= f.isDirectory() ? "" : f.length() %></td>
      <td><%= sdf.format(new Date(f.lastModified())) %></td>
      <td>
        <%
          if (!f.isDirectory()) {
        %>
          <a href="<%=request.getContextPath()%>/download?path=<%= enc %>">скачать</a>
        <%
          } else { out.print(""); }
        %>
      </td>
    </tr>
  <%
      }
    }
  %>
  </table>
</body>
</html>