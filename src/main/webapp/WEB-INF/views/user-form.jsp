<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="header.jspf" %>
<c:set var="editing" value="${not empty target}"/>
<div class="row justify-content-center"><div class="col-md-6">
  <div class="card shadow-sm"><div class="card-body p-4">
    <h3 class="mb-3">${editing ? 'Update Profile' : 'Register'}</h3>
    <form method="post" action="${pageContext.request.contextPath}/${editing ? 'users' : 'register'}">
      <c:if test="${editing}">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="id" value="${target.id}">
      </c:if>
      <div class="mb-3"><label class="form-label">Username</label>
        <input name="username" class="form-control" value="<c:out value='${editing ? target.username : param.username}'/>"
               ${editing ? 'readonly' : 'required'}></div>
      <div class="mb-3"><label class="form-label">Full name</label>
        <input name="fullName" class="form-control" required value="<c:out value='${editing ? target.fullName : param.fullName}'/>"></div>
      <div class="mb-3"><label class="form-label">Email</label>
        <input name="email" type="email" class="form-control" required value="<c:out value='${editing ? target.email : param.email}'/>"></div>
      <div class="mb-3"><label class="form-label">Phone</label>
        <input name="phone" class="form-control" value="<c:out value='${editing ? target.phone : param.phone}'/>"></div>
      <div class="mb-3"><label class="form-label">Password ${editing ? '(leave blank to keep current)' : ''}</label>
        <input name="password" type="password" class="form-control" ${editing ? '' : 'required'}></div>
      <button class="btn btn-dark">${editing ? 'Save changes' : 'Register'}</button>
      <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/books">Cancel</a>
    </form>
  </div></div>
</div></div>
<%@ include file="footer.jspf" %>
