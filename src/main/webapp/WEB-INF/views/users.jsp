<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="header.jspf" %>
<div class="d-flex justify-content-between align-items-center mb-3">
  <h3>Users</h3>
  <form class="d-flex" method="get" action="${pageContext.request.contextPath}/users">
    <input name="q" class="form-control me-2" placeholder="Search name / username / ID" value="<c:out value='${q}'/>">
    <button class="btn btn-outline-dark">Search</button>
  </form>
</div>
<div class="table-responsive"><table class="table table-striped bg-white align-middle">
  <thead class="table-dark"><tr><th>ID</th><th>Username</th><th>Name</th><th>Email</th><th>Phone</th><th>Role</th><th></th></tr></thead>
  <tbody>
  <c:forEach var="u" items="${users}">
    <tr>
      <td>${u.id}</td><td><c:out value="${u.username}"/></td><td><c:out value="${u.fullName}"/></td>
      <td><c:out value="${u.email}"/></td><td><c:out value="${u.phone}"/></td>
      <td><span class="badge ${u.admin ? 'bg-danger' : 'bg-secondary'}">${u.role}</span></td>
      <td class="text-end">
        <a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/users?action=edit&id=${u.id}">Edit</a>
        <c:if test="${u.id ne sessionScope.user.id}">
          <form class="d-inline" method="post" action="${pageContext.request.contextPath}/users"
                onsubmit="return confirm('Delete this user?')">
            <input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="${u.id}">
            <button class="btn btn-sm btn-outline-danger">Delete</button></form>
        </c:if>
      </td>
    </tr>
  </c:forEach>
  <c:if test="${empty users}"><tr><td colspan="7" class="text-center text-muted">No users found.</td></tr></c:if>
  </tbody>
</table></div>
<%@ include file="footer.jspf" %>
