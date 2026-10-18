<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="header.jspf" %>
<div class="row justify-content-center"><div class="col-md-5">
  <div class="card shadow-sm"><div class="card-body p-4">
    <h3 class="mb-3">Login</h3>
    <form method="post" action="${pageContext.request.contextPath}/login">
      <div class="mb-3"><label class="form-label">Username</label>
        <input name="username" class="form-control" required autofocus></div>
      <div class="mb-3"><label class="form-label">Password</label>
        <input name="password" type="password" class="form-control" required></div>
      <button class="btn btn-dark w-100">Login</button>
    </form>
    <p class="mt-3 mb-0 text-muted small">New here? <a href="${pageContext.request.contextPath}/register">Create an account</a></p>
  </div></div>
</div></div>
<%@ include file="footer.jspf" %>
